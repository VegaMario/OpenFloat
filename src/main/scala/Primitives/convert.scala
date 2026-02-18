package Primitives

import FloatingPoint.{FloatingPointFormat, FP16, FP32, FP64, FP128}

object convert {
  // ---------------------------------------------------------------------------
  // IEEE 754 bit-pattern  →  Decimal
  // ---------------------------------------------------------------------------

  /** Primary overload — accepts any [[FloatingPointFormat]]. */
  def convert_IEEE754_to_Decimal(num: BigInt, fmt: FloatingPointFormat): BigDecimal = {
    val exponent = fmt.exponent
    val mantissa = fmt.mantissa
    val bw       = fmt.bw
    val bias     = BigDecimal(fmt.bias)

    // Negative-zero and positive-zero early return
    if (num == (BigInt(1) << (bw - 1)) || num == BigInt(0))
      return BigDecimal(0)

    // Pad the binary representation to full bit-width (LSB-first)
    val bits = num.bigInteger.toString(2).reverse.padTo(bw, '0')

    val sign       = if (bits(bw - 1) == '1') BigDecimal(-1) else BigDecimal(1)
    val exp_bits   = bits.slice(mantissa, bw - 1).reverse
    val exp_value  = BigDecimal(BigInt(exp_bits, 2))
    val mant_bits  = "1" + bits.slice(0, mantissa).reverse
    val mant_value = BigDecimal(BigInt(mant_bits, 2)) * BigDecimal(2).pow(-mantissa)

    mant_value * BigDecimal(2).pow((exp_value - bias).toInt) * sign
  }

  // ---------------------------------------------------------------------------
  // Decimal string  →  IEEE 754 bit-pattern
  // ---------------------------------------------------------------------------

  /** Primary overload — accepts any [[FloatingPointFormat]]. */
  def convert_string_to_IEEE_754(str: String, fmt: FloatingPointFormat): BigInt = {
    val exponent = fmt.exponent
    val mantissa = fmt.mantissa
    val bias     = fmt.bias.toInt
    val max_val  = (BigDecimal(2) - BigDecimal(2).pow(-mantissa)) * BigDecimal(2).pow(bias)
    val min_val  = BigDecimal(2).pow(-(bias - 1))

    // Clamp to representable range, extract absolute value and sign
    val (num, sign) = BigDecimal(str) match {
      case x if x.abs > max_val                => (max_val, x.sign)
      case x if x.abs < min_val && x.abs != 0  => (min_val, x.sign)
      case x                                    => (x.abs,   x.sign)
    }

    // Split into whole and fractional parts
    val whole     = num.toBigInt
    val whole_str = whole.toString(2)
    val base_exp  = whole_str.length - 1 + bias

    // Convert fractional part to binary digits via repeated doubling
    val (frac_str, _) = (0 until mantissa).foldLeft(("", num - BigDecimal(whole))) {
      case ((acc, rem), _) =>
        val doubled = rem * 2
        val bit     = doubled.toBigInt
        (acc + bit.toString, doubled - BigDecimal(bit))
    }

    val full_str = whole_str + frac_str
    val ms_one   = full_str.indexWhere(_ == '1')

    val (new_sign, new_exp, new_frac) =
      if (ms_one == -1) {
        // Value is zero
        ("0", "0" * exponent, "0" * mantissa)
      } else {
        val f_padded       = full_str.drop(ms_one + 1) + ("0" * mantissa)
        val exp_int        = base_exp - ms_one
        val exp_bin_string = exp_int.toBinaryString
          .reverse.padTo(exponent, '0').reverse
          .takeRight(exponent)
        val sign_str       = if (sign < 0) "1" else "0"
        (sign_str, exp_bin_string, f_padded.take(mantissa))
      }

    BigInt(new_sign + new_exp + new_frac, 2)
  }

  // ---------------------------------------------------------------------------
  // Usage example
  // ---------------------------------------------------------------------------

  private def usage_example(): Unit = {
    // Using a FloatingPointFormat directly
    val test_str_to_IEEE = convert_string_to_IEEE_754(BigDecimal("12.1234E2").bigDecimal.toPlainString, FP32)
    val test_IEEE_to_Dec = convert_IEEE754_to_Decimal(test_str_to_IEEE, FP32)
    println(s"${test_str_to_IEEE.toString(16)}")
    println(s"${test_IEEE_to_Dec}")
  }
}

package Primitives

object convert {
  // FP conversion IEEE 754
  def convert_IEEE754_to_Decimal(num: BigInt, bw: Int): BigDecimal = {
    val (exponent, mantissa) = bw match {
      case 16 => (5,10)
      case 32 => (8,23)
      case 64 => (11,52)
      case 128 => (15,112)
    }
    if (num == BigInt(1) << (bw-1) || num  == BigInt(0))
      return BigDecimal(num)

    val bias = BigDecimal(2).pow(exponent - 1) - 1
    val list = num.bigInteger.toString(2).reverse.padTo(bw, '0')

    val sign = BigDecimal(-1).pow(list(bw-1).toInt)
    val exp_string = list.slice(mantissa, bw - 1)
    val exp_double = BigDecimal(BigInt(exp_string.reverse, 2))
    val mant_string = "1" + list.slice(0, mantissa).reverse
    var mant_double = BigDecimal(BigInt(mant_string, 2)) * BigDecimal(2).pow(-mantissa)
    mant_double = mant_double * BigDecimal(2).pow((exp_double - bias).toInt) * sign
    mant_double
  }

  def convert_string_to_IEEE_754(str: String, bw: Int): BigInt = {
    val (exponent, mantissa) = bw match {
      case 16 => (5,10)
      case 32 => (8,23)
      case 64 => (11,52)
      case 128 =>(15,112)
    }
    val bias = (BigDecimal(2).pow(exponent - 1) - 1).toInt
    val max_val = (BigDecimal(2) - BigDecimal(2).pow(-1 * mantissa)) * BigDecimal(2).pow(bias)
    val min_val = BigDecimal(2).pow(-1 * (bias - 1))

    val (num, sign) = BigDecimal(str) match {
      case x if (x.abs > max_val) => (max_val, x.sign)
      case x if (x.abs < min_val && x.abs != 0) => (min_val, x.sign)
      case x if true => (x.abs, x.sign)
    }

    val (w_BigInt, f_BigDec) = (num.toBigInt, num - BigDecimal(num.toBigInt))
    val whole = w_BigInt
    var frac = f_BigDec

    val whole_str = whole.toString(2)
    val base_exp = (whole_str.length - 1 + bias)

    val frac_str = (0 until mantissa).map(i=>{
      frac = frac * 2
      val msb = frac.toBigInt
      frac = frac - BigDecimal(msb)
      msb.toString()
    }).reduce(_+_)

    val full_str = whole_str + frac_str

    val ms_one = (full_str).indexWhere(_ == '1')

    val (new_sign, new_exp, new_frac) =
      if(ms_one == -1)
        ("0", Seq.fill(exponent)("0").reduce(_+_), Seq.fill(mantissa)("0").reduce(_+_))
      else {
        val f_temp = full_str.slice(ms_one+1,full_str.length) + Seq.fill(mantissa)("0").reduce(_+_)
        var exp_temp = base_exp - ms_one
        val exp_temp_string = (0 until exponent).map(i => {
          val t =  (exp_temp % 2).toString
          exp_temp = exp_temp / 2
          t
        }).reverse.reduce(_+_)
        val sign_temp = if(sign < 0 ) "1" else "0"
        (sign_temp, exp_temp_string, f_temp.slice(0, mantissa))
      }

    BigInt(new_sign + new_exp + new_frac, 2)
  }


  private def usage_example(): Unit = {
    val test_str_to_IEEE = convert_string_to_IEEE_754(BigDecimal("12.1234E2").bigDecimal.toPlainString,32)
    val test_IEEE_to_Dec = convert_IEEE754_to_Decimal(test_str_to_IEEE, 32)
    println(s"${test_str_to_IEEE.toString(16)}")
    println(s"${test_IEEE_to_Dec}")
  }
}
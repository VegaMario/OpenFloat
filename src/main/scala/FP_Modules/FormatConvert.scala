package FP_Modules

object FormatConvert {
  // FP conversion IEEE 754
  def convert_IEEE754_to_Decimal(num: BigInt, bw: Int): BigDecimal = {
    val (exponent, mantissa, zero_1) = bw match {
      case 16 => (5,10,BigInt("8000", 16))
      case 32 => (8,23,BigInt("80000000", 16))
      case 64 => (11,52,BigInt("8000000000000000", 16))
      case 128 => (15,112,BigInt("80000000000000000000000000000000", 16))
    }

    var n = num
    val list = (0 until bw).map(i=>{
      val t = (n % 2).toString
      n = n / 2
      t
    })

    val sign_string = list.toList(bw - 1)
    val exp_string = list.slice(mantissa, bw - 1).reduce(_ + _) // concat all exp bits into a string
    val exp_double = binary_string_to_Double(exp_string) // converts the exp string into a BigDecimal
    val mant_string = list.slice(0, mantissa).reduce(_ + _).reverse
    var mant_double = binary_string_to_Double_Frac(mant_string) // converts the mantissa string into a BigDecimal
    mant_double = mant_double * BigDecimal(2).pow((exp_double - (Math.pow(2, exponent - 1) - 1)).toInt) //Math.pow(2, (sum-(Math.pow(2, exponent - 1) - 1)).toDouble)
    if (sign_string.equals("1"))
      mant_double = mant_double * -1
    if (num == zero_1 || num  == BigInt(0)) // if num is zero/negative zero
      BigDecimal(0)
    else
      mant_double
  }

  def binary_string_to_Double(str: String): BigDecimal = {
    (0 until str.length).map(i => {
      if (str(i).equals('1'))
        BigDecimal(2).pow(i)
      else
        BigDecimal(0.0)
    }).sum
  }

  def binary_string_to_Double_Frac(str: String): BigDecimal = {
    (1 to str.length).map(i=>{
      if (str(i - 1).equals('1'))
        BigDecimal(2).pow(-1 * i)
      else
        BigDecimal(0.0)
    }).sum + 1.0
  }

  def convert_string_to_IEEE_754(str: String, bw: Int): BigInt = {
    val (exponent, mantissa, bias) = bw match {
      case 16 => (5,10,15)
      case 32 => (8,23,127)
      case 64 => (11,52,1023)
      case 128 =>(15,112,16383)
    }

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


  def usage_example(): Unit = {
    val test_str_to_IEEE = convert_string_to_IEEE_754(BigDecimal("12.1234E2").bigDecimal.toPlainString,32)
    val test_IEEE_to_Dec = convert_IEEE754_to_Decimal(test_str_to_IEEE, 32)
    println(s"${test_str_to_IEEE.toString(16)}")
    println(s"${test_IEEE_to_Dec}")
  }
}
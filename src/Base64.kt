open class Base64 {
    private val capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val smallLetters = "abcdefghijklmnopqrstuvwxyz"
    private val numbers = "0123456789"
    private val minus = "-"
    private val slash = "_"
    private val bs = arrayOf(capitals, smallLetters, numbers, minus, slash).joinToString(separator = "")

    private fun getIndex(c: Char): Int {
        return bs.indexOf(c)
    }

    private fun convert(s: String, skip: Int): String {
        val sb = StringBuilder()
        val buf = arrayOf(getIndex(s[0]), getIndex(s[1]), getIndex(s[2]), getIndex(s[3]))
        val fst = ((buf[0] shl 2) or (buf[1] ushr 4)) and 255
        val sec = (((buf[1] and 15) shl 4) or (buf[2] ushr 2)) and 255
        val thr = (((buf[2] and 3) shl 6) or buf[3]) and 255
        sb.append(fst.toChar())
        if(skip in 0 until 3) sb.append(sec.toChar())
        if(skip in 0 until 2) sb.append(thr.toChar())

        return sb.toString()
    }

    open fun decode(s: String): String {
        val sb = StringBuilder()
        val n = s.length
        val buf = arrayOf(bs[0], bs[0], bs[0], bs[0])
        for(i in 0 until n step 4) {
            var skip = 0
            for(bufIndex in 0 until 4) {
                buf[bufIndex] = if(i + bufIndex < n) s[i + bufIndex] else {
                    skip += 1
                    bs[0]
                }
            }
            sb.append(convert(buf.joinToString(separator = ""), skip))
        }

        return sb.toString()
    }
}
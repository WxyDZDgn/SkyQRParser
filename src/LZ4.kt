open class LZ4 {
    private class LZ4Node(val left: Int, val right: Int) {
        operator fun component1() = left
        operator fun component2() = right
    }
    private var s: String

    constructor(s: String) {
        this.s = s
    }

    private fun hiHyte(x: Int): Int {
        return (x ushr 4) and 15
    }

    private fun loHyte(x: Int): Int {
        return (x and 15)
    }

    private fun getAdvance(i: Int): LZ4Node {
        val n = s.length
        var extra = 0

        var j = i
        while(j < n) {
            val b = s[j].code
            require(b in 0..255)
            extra += b
            if(b != 255) break
            j += 1
        }
        return LZ4Node(j + 1, extra)
    }

    private fun byteToHytesSplit(x: Int): LZ4Node {
        return LZ4Node(hiHyte(x), loHyte(x))
    }

    open fun uncompress(): String {
        val sb = StringBuilder()
        val n = s.length
        var i = 0
        while(i < n) {
            val data = s[i].code
            val (hi, lo) = byteToHytesSplit(data)
            // token and olb
            var length = hi
            i += 1
            if(i >= n) break
            if(hi == 15) {
                // olb
                val (idx, extra) = getAdvance(i)
                i = idx
                length += extra
            }
            sb.append(s.subSequence(i, i + length))
            i += length
            // offset
            if(i >= n) break
            val loByte = s[i].code and 255
            if(i + 1 >= n) break
            val hiByte = s[i + 1].code and 255
            i += 2
            val offset = ((hiByte shl 8) or (loByte)) and 65535
            // match length
            length = lo + 4
            if(lo == 15) {
                val (idx, extra) = getAdvance(i)
                i = idx
                length += extra
            }
            val sbLength = sb.length
            val tmp = sb.toString()
            sb.append(tmp.substring(sbLength - offset, sbLength - offset + length))
        }

        return sb.toString()
    }
}
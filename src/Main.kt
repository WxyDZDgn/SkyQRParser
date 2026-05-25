fun main() {
    val base = "8R..." // s 为二维码解析出后面o参数的Base64, 如 `https://sky.thatg.co/o=8R...`
    val decode = Base64().decode(base) // 将Base64转换成LZ4压缩后的编码
    val json = LZ4(decode).uncompress() // 将压缩编码解压, 得到JSON数据, 如 `{"body":{...},"wing":{...},...}`
    println(json)
}

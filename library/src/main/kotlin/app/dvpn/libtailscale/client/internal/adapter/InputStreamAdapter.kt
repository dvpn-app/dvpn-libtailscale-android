package app.dvpn.libtailscale.client.internal.adapter


import java.io.InputStream

internal class InputStreamAdapter(
    private val inputStream: InputStream
) : libtailscale.InputStream {

    constructor(bytes: ByteArray) : this(bytes.inputStream())

    override fun read(): ByteArray? {
        val b = ByteArray(4096)
        val i = inputStream.read(b)
        if (i == -1) {
            return null
        }

        return b.sliceArray(0 ..< i)
    }

    override fun close() {
        inputStream.close()
    }
}

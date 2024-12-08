package chatprogramV2

import java.io.*
import java.net.Socket

object SocketCloseUtil {

    fun closeAll(socket: Socket?, input: BufferedReader, output: PrintWriter) {
        close(input)
        close(output)
        close(socket)
    }

    private fun close(input: BufferedReader?) {
        try {
            input?.close()
        } catch (e: IOException) {
            e.message?.let { println(it) }
        }
    }

    private fun close(output: PrintWriter?) {
        try {
            output?.close()
        } catch (e: IOException) {
            e.message?.let { println(it)  }
        }
    }

    private fun close(socket: Socket?) {
        try {
            socket?.close()
        } catch (e: IOException) {
            e.message?.let { println(it) }
        }
    }
}
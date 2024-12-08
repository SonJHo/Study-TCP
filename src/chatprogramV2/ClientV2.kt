package chatprogramV2

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.util.Scanner


private const val PORT = 12345
private const val ADDRESS = "localhost"

fun main() {
    val socket = Socket()
    socket.connect(InetSocketAddress(ADDRESS, PORT), 3000)

    try{
        socket.use { sk->
            println("connect success")
            printManual()
            BufferedReader(InputStreamReader(sk.getInputStream())).use { br->
                PrintWriter(OutputStreamWriter(sk.getOutputStream()), true).use { pw ->
                    Scanner(System.`in`).use { sc->
                        try {
                            val thread = Thread(Receiver(br))
                            thread.start()
                            while (true) {
                                val line = sc.nextLine()
                                if (!thread.isAlive) {
                                    break
                                }
                                pw.println(line)
                            }
                        } catch (e: IOException) {
                            println(e)
                        } finally {
                            println("종료")
                        }
                    }
                }
            }
        }
    }catch (e: IOException){
        println(e)
    }

}

fun printManual() {
    println(" ===command list===\n" +
            " join|{name}\n" +
            " message|{data}\n" +
            " change|{name} \n" +
            " exit\n" +
            "---------------------")
}


class Receiver(private val br: BufferedReader) : Runnable {
    override fun run() {
        try {
            while (true) {
                val line = br.readLine() ?: break
                println(line)
            }
        } catch (e: IOException) {
            throw e
        }
    }
}

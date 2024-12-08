package chatprogramV2

import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.Executors


private val PORT = 12345
fun main() {

    println("server open..")
    val serverSocket = ServerSocket(PORT)

    val sessionManager = SessionManager()

    println("server listen.. ${PORT}")

    //shutdownHook
    val shutdownHook = ShutdownHook(serverSocket, sessionManager)
    Runtime.getRuntime().addShutdownHook(Thread(shutdownHook, "shutdown"))

    try {
        Executors.newCachedThreadPool().use { threadPool->
            while (true) {
                val socket = serverSocket.accept()
                println("connect success $socket")
                val session = Session(socket, sessionManager)
                threadPool.execute(session)
                sessionManager.add(session)
            }
        }
    }catch (e: IOException){
        println("server socket close")
    }

}

class ShutdownHook(private val serverSocket: ServerSocket, val sessionManager: SessionManager) : Runnable {
    override fun run() {
        println("shutdownHook")
        serverSocket.close()
        Thread.sleep(1000)
    }
}

fun broadcasting(line: String, sessionManager: SessionManager) {
    val sessions = sessionManager.sessions
    for (session in sessions) {
        session.pw.println(line)
    }

}


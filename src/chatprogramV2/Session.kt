package chatprogramV2

import chatprogramV2.SocketCloseUtil.closeAll
import java.io.*
import java.lang.StringBuilder
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.NoSuchElementException

class Session(private val socket: Socket, private val sessionManager: SessionManager) : Runnable {
    private val br = BufferedReader(InputStreamReader(socket.getInputStream()))
    val pw = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
    private var closed = false
    private var clientName = "unknown"
    override fun run() {
        try {
            while (true) {
                try{
                    val line = br.readLine()
                    println("$clientName: $line")
                    val st = StringTokenizer(line, "|")
                    val command = st.nextToken().trim()
                    if (command == CommandOption.JOIN.cmd || command == CommandOption.CHANGE.cmd) {
                        setClientName(st.nextToken())
                    } else if (command == CommandOption.MASSAGE.cmd) {
                        broadcasting("$clientName: ${st.nextToken()}", sessionManager)
                    } else if (command == CommandOption.USERS.cmd) {
                        showAllUser()
                    } else if (command == CommandOption.EXIT.cmd) {
                        broadcasting("$clientName is quit", sessionManager)
                        break
                    } else {
                        throw NoSuchElementException()
                    }
                }catch (e : NoSuchElementException){
                    pw.println("command error")
                }
            }
        } catch (e: NoSuchElementException) {
            pw.println("command error")
        } catch (e: SocketException) {
            println("socket error")
        } catch (e: IOException) {
            println(e)
        } finally {
            println("$clientName disconnect")
            close()
        }
    }


    private fun setClientName(newName: String) {
        val oldName = clientName
        clientName = newName
        pw.println("resetName: $oldName -> $clientName")
    }

    private fun showAllUser() {
        val sb = StringBuilder()
        sb.append("==참가자 목록==\n")
        for (session in sessionManager.sessions) {
            sb.append("${session.clientName}\n")
        }
        sb.append("--------------")
        if (sb.isNotEmpty()) {
            pw.println(sb)
        }
    }

    @Synchronized
    fun close() {
        sessionManager.remove(this)
        if (closed) {
            return
        }
        closeAll(socket, br, pw)
        closed = true
        println("disconnect , resources free")
    }

}
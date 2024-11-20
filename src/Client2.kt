import java.awt.BorderLayout
import java.awt.Font
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.UncheckedIOException
import java.net.Socket
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField

fun main() {
    val frame = JFrame("Chat frame")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(600, 600)

    val menuBar = JMenuBar()
    val m1 = JMenu("File")
    val m2 = JMenu("help")
    val connect_bt = JButton("connect")

    menuBar.add(m1)
    menuBar.add(m2)
    menuBar.add(connect_bt)
    val m11 = JMenuItem("open")
    val m22 = JMenuItem("save as")
    val helpItem = JMenuItem("Guide")
    m1.add(m11)
    m2.add(m22)
    m2.add(helpItem)

    val panel = JPanel()
    val label = JLabel("enter Text")
    val tf = JTextField(20)
    val send_bt = JButton("send")
    val reset_bt = JButton("reset")
    val quit_bt = JButton("quit")
    panel.add(label)
    panel.add(tf)
    panel.add(send_bt)
    panel.add(reset_bt)
    panel.add(quit_bt)

    val address = "192.168.45.69"
    var socket = Socket()
    var br_input: BufferedReader
    var pw: PrintWriter? = null
    var username: String


    val ta = JTextArea()
    ta.lineWrap = true
    ta.wrapStyleWord = true
    ta.isEditable = false
    val scrollPane = JScrollPane(ta)

    val currentFont = ta.font
    ta.font = Font(currentFont.fontName, currentFont.style, 20)

    tf.addActionListener {
        send_bt.doClick()
    }
    reset_bt.addActionListener {
        ta.text = ""
    }
    quit_bt.addActionListener{
        pw!!.println("quit")
        tf.text = ""
        ta.append("연결이 끊어졌습니다\n")
        socket.close()
    }
    send_bt.addActionListener {
        val inputText = tf.text
        pw!!.println(inputText)

        tf.text = ""
    }
    helpItem.addActionListener {
        JOptionPane.showMessageDialog(frame, "사용법")
    }



    connect_bt.addActionListener {
        try {
            socket = Socket(address, 8888)

            pw = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
            br_input = BufferedReader(InputStreamReader(System.`in`))

            username = JOptionPane.showInputDialog("enter your Nickname : ")
            pw!!.println(username)

            Thread(MessageReceiverV2(socket, ta)).start()


        } catch (e: UncheckedIOException) {
            println("error")
        } catch (e: IOException) {
            println("connect error!")
        } finally {
        }
    }


    frame.contentPane.add(BorderLayout.NORTH, menuBar)
    frame.contentPane.add(BorderLayout.SOUTH, panel)
    frame.contentPane.add(BorderLayout.CENTER, scrollPane)
    frame.isVisible = true
}
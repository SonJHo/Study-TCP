package chatprogramV2

enum class CommandOption (val cmd :String) {
    JOIN("join"),
    MASSAGE("message"),
    CHANGE("change"),
    USERS("users"),
    EXIT("exit")
}
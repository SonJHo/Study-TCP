package chatprogramV2

class SessionManager {
    val sessions = ArrayList<Session>()


    @Synchronized
    fun add(session: Session){
        sessions.add(session)
    }
    @Synchronized
    fun remove(session: Session){
        sessions.remove(session)
    }

    @Synchronized
    fun closeAll(){
        for (session in sessions) {
            session.close()
        }
        sessions.clear()
    }
}
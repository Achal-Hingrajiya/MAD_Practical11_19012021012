package src
class Notes(
        var title:String,
        var subTitle:String,
        var Description:String,
        var modifiedTime:String,
        var isReminder: Boolean = false)
{

    var remindertime: Long = System.currentTimeMillis()
    var id = noteIdGeneration()

    companion object {

        var idNote = 0

        fun noteIdGeneration(): Int {
            idNote++
            return idNote
        }
        fun addNote(note: Notes){
            notesArray.add(note)
        }
        var notesArray: ArrayList<Notes> = ArrayList()
    }
}


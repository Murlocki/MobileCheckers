import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobilecheckers.models.Player

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Константы для базы данных
    companion object {
        const val DATABASE_NAME = "checkers_db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "players"
        const val COLUMN_ID = "id"
        const val COLUMN_NICKNAME = "nickname"
        const val COLUMN_WINS = "wins"
        const val COLUMN_LOSSES = "losses"
        const val COLUMN_AVERAGE_MOVES = "average_moves"
        const val COLUMN_RATING = "rating"
    }

    // SQL запрос на создание таблицы
    private val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NICKNAME TEXT NOT NULL,
            $COLUMN_WINS INTEGER DEFAULT 0,
            $COLUMN_LOSSES INTEGER DEFAULT 0,
            $COLUMN_AVERAGE_MOVES REAL DEFAULT 0.0,
            $COLUMN_RATING REAL DEFAULT 0.0
        )
    """

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addPlayer(player: Player): Long {
        val db = writableDatabase
        val values = ContentValues()

        // Сначала вычисляем рейтинг перед вставкой
        values.put(COLUMN_NICKNAME, player.nickname)
        values.put(COLUMN_WINS, player.wins)
        values.put(COLUMN_LOSSES, player.losses)
        values.put(COLUMN_AVERAGE_MOVES, player.averageMoves)
        values.put(COLUMN_RATING, player.rating)  // Добавляем рейтинг

        // Вставляем данные в таблицу
        return db.insert(TABLE_NAME, null, values)
    }

    // Получение всех игроков
    fun getAllPlayers(): List<Player> {
        val playerList = mutableListOf<Player>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nickname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NICKNAME))
                val wins = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WINS))
                val losses = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOSSES))
                val averageMoves = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AVERAGE_MOVES))

                playerList.add(Player(nickname, wins, losses, averageMoves,id))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return playerList
    }

    // Получение игрока по id
    fun getPlayerById(id: Long): Player? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val idColumnIndex = cursor.getColumnIndex(COLUMN_ID)
            val nicknameColumnIndex = cursor.getColumnIndex(COLUMN_NICKNAME)
            val winsColumnIndex = cursor.getColumnIndex(COLUMN_WINS)
            val lossesColumnIndex = cursor.getColumnIndex(COLUMN_LOSSES)
            val averageMovesColumnIndex = cursor.getColumnIndex(COLUMN_AVERAGE_MOVES)
            val ratingColumnIndex = cursor.getColumnIndex(COLUMN_RATING)

            if (idColumnIndex != -1 && nicknameColumnIndex != -1 && winsColumnIndex != -1 && lossesColumnIndex != -1 &&
                averageMovesColumnIndex != -1 && ratingColumnIndex != -1) {

                val id = cursor.getLong(idColumnIndex)
                val nickname = cursor.getString(nicknameColumnIndex)
                val wins = cursor.getInt(winsColumnIndex)
                val losses = cursor.getInt(lossesColumnIndex)
                val averageMoves = cursor.getDouble(averageMovesColumnIndex)
                val rating = cursor.getDouble(ratingColumnIndex)

                return Player(nickname, wins, losses, averageMoves, id)
            } else {
                return null
            }
        }
        cursor.close()
        return null

        cursor.close()
        return null
    }

    // Обновление данных игрока
    fun updatePlayer(player: Player): Int {
        val db = writableDatabase
        val values = ContentValues()

        // Пересчитываем рейтинг перед обновлением
        values.put(COLUMN_NICKNAME, player.nickname)
        values.put(COLUMN_WINS, player.wins)
        values.put(COLUMN_LOSSES, player.losses)
        values.put(COLUMN_AVERAGE_MOVES, player.averageMoves)
        values.put(COLUMN_RATING, player.rating)  // Обновляем рейтинг

        // Обновляем данные игрока в базе
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(player.id.toString())
        )
    }
    // Удаление игрока
    fun deletePlayer(playerId: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(playerId.toString())
        )
    }
}

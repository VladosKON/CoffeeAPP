package com.example.coffeeapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.coffeeapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import javax.inject.Inject
import javax.inject.Provider


//DB с записями
@Database(entities = [Note::class, Tag::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao

    class Callback @Inject constructor(
        private val database: Provider<NoteDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val noteDao = database.get().noteDao()
            val tagDao = database.get().tagDao()

            applicationScope.launch {
                noteDao.insert(
                    Note(
                        //Кения
                        title = "Помощь и обучение",
                        roastDate = "30.04.2021",
                        roastInfo = "Чтобы добавить страну: введите информацию в поле и нажмите пробел." +
                                " Все остальные данные сохранятся после нажатия на галочку автоматически." +
                                " Для удаления кофе просто свайпните его в сторону",
                        dryAr = "Ввод сухого аромата",
                        dryArSB = 3,
                        wetAr = "Ввод влажного аромата",
                        wetArSB = 4,
                        sweet = "Дескрипторы сладости",
                        sweetSB = 3,
                        acid = "Десткрипторы кислотности",
                        acidSB = 5,
                        body = "Описание тела",
                        bodySB = 2,
                        balanceSB = 3,
                        flavour = "Сюда запишите впечатление о кофе и дескрипторы, которые вы нашли",
                        lastDate = LocalDateTime.of(2021, 4, 30, 0, 0, 0)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    )
                )
//                noteDao.insert(
//                    Note(
//                        //Колумбия
//                        title = "Колумбия Уила",
//                        roastDate = "04.01.20",
//                        roastInfo = "Ноты красного яблока, темного винограда и какао с яркой фруктовой кислотностью",
//                        dryAr = "Фрукты",
//                        dryArSB = 4,
//                        wetAr = "Ягоды",
//                        wetArSB = 5,
//                        sweet = "Яблоко и виноград",
//                        sweetSB = 5,
//                        acid = "Фрукты",
//                        acidSB = 4,
//                        body = "Шероховатое",
//                        bodySB = 4,
//                        balanceSB = 3,
//                        flavour = "Красное яблоко, виноград",
//                        lastDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
//                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//                    )
//                )
//                noteDao.insert(
//                    Note(
//                        //Бразилия
//                        title = "Бразилия Инасиу Урбан",
//                        roastDate = "21.12.20",
//                        roastInfo = "Плотный и сладкий кофе с нотами абрикоса, ореховой пасты и желтого яблока",
//                        dryAr = "Абрикос",
//                        dryArSB = 5,
//                        wetArSB = 1,
//                        wetAr = "Фрукты",
//                        sweet = "Абрикос и яблоко",
//                        sweetSB = 5,
//                        acid = "Яблоко",
//                        acidSB = 1,
//                        body = "Плотное, вяжущее",
//                        bodySB = 5,
//                        balanceSB = 3,
//                        flavour = "Абрикос, желтое яблоко",
//                        lastDate = LocalDateTime.of(2019, 3, 22, 0, 0, 0)
//                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//                    )
//                )
//                noteDao.insert(
//                    Note(
//                        //Бразилия
//                        title = "Бразилия Эльдорадо",
//                        roastDate = "11.09.2019",
//                        roastInfo = "Плотный и сладкий кофе с нотами персика, изюма и темного рома",
//                        dryAr = "Персик",
//                        dryArSB = 1,
//                        wetArSB = 2,
//                        wetAr = "Алкоголь",
//                        sweet = "Персик, изюм",
//                        sweetSB = 5,
//                        acid = "Изюм",
//                        acidSB = 1,
//                        body = "Гладкое",
//                        bodySB = 4,
//                        balanceSB = 3,
//                        flavour = "Персик, изюм и ром",
//                        lastDate = LocalDateTime.of(2022, 2, 19, 0, 0, 0)
//                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//                    )
//                )
                tagDao.insert(
                    Tag(
                        name = "Кения",
                        note_id = 1
                    )
                )
//                tagDao.insert(
//                    Tag(
//                        name = "Колумбия",
//                        note_id = 2
//                    )
//                )
//                tagDao.insert(
//                    Tag(
//                        name = "Бразилия",
//                        note_id = 3
//                    )
//                )
//                tagDao.insert(
//                    Tag(
//                        name = "Бразилия",
//                        note_id = 4
//                    )
//                )
            }
        }
    }
}
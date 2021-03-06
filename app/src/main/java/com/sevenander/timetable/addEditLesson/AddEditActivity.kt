package com.sevenander.timetable.addEditLesson

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sevenander.timetable.R
import com.sevenander.timetable.data.model.LessonEntity
import com.sevenander.timetable.util.Const
import kotlinx.android.synthetic.main.activity_add_edit.*
import java.util.*

class AddEditActivity : AppCompatActivity(), AddEditView {

    private var timePicker: TimePickerDialog? = null

    private var editMode: Boolean = false
    private var lessonId: Int = -1
    private var dayNumber: Int = -1

    private var presenter: AddEditPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        init()
    }

    override fun onDestroy() {
        presenter?.destroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.menu_action_save) {
            presenter?.saveClicked(lessonId, etLessonName.text.toString(),
                    etLessonTeacher.text.toString(), dayNumber,
                    tvLessonStart.text.toString(),
                    tvLessonEnd.text.toString())
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showProgress() {
        pbLesson.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbLesson.visibility = View.GONE
    }

    override fun showError(message: String) {
        tvMessage.text = message
        tvMessage.visibility = View.VISIBLE
    }

    override fun hideError() {
        tvMessage.visibility = View.GONE
    }

    override fun showLesson(lesson: LessonEntity) {
        dayNumber = lesson.day.ordinal
        etLessonName.setText(lesson.name)
        etLessonTeacher.setText(lesson.teacher)
        tvLessonDay.text = lesson.day.name.toLowerCase().capitalize()
        tvLessonStart.text = lesson.startTime
        tvLessonEnd.text = lesson.endTime

        llLesson.visibility = View.VISIBLE
    }

    override fun hideLesson() {
        llLesson.visibility = View.GONE
    }

    override fun showTimePickerDialog() {
        if (timePicker == null || !(timePicker as TimePickerDialog).isShowing) {
            val calendar = Calendar.getInstance()
            timePicker = TimePickerDialog(context(), presenter, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), true)
            timePicker?.show()
        }
    }

    override fun hideTimePickerDialog() {
        if (timePicker != null && (timePicker as TimePickerDialog).isShowing) {
            timePicker?.dismiss()
        }
    }

    override fun setStartTime(startTime: String) {
        tvLessonStart.text = startTime
    }

    override fun setEndTime(endTime: String) {
        tvLessonEnd.text = endTime
    }

    override fun onLessonSaved() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun context(): Context {
        return this@AddEditActivity
    }

    private fun init() {
        getExtras()

        presenter = AddEditPresenter(this@AddEditActivity)
        if (editMode)
            presenter?.init(lessonId)


        tvLessonStart.setOnClickListener({
            presenter?.pickTimeClick(true)
        })

        tvLessonEnd.setOnClickListener({
            presenter?.pickTimeClick(false)
        })
    }

    private fun getExtras() {
        editMode = intent.extras.containsKey(Const.KEY_LESSON_ID)
        if (editMode) {
            lessonId = intent.extras.getInt(Const.KEY_LESSON_ID)
        } else {
            dayNumber = intent.extras.getInt(Const.KEY_LESSON_DAY)
        }
    }
}

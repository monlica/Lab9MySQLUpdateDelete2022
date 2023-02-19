package com.example.lab9mysqlupdatedelete2022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab9mysqlupdatedelete2022.databinding.ActivityShowEditDeleteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowEditDeleteActivity : AppCompatActivity() {
    private lateinit var bindingShow : ActivityShowEditDeleteBinding
    var studentList = arrayListOf<Student>()
    val createClient = StudentAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingShow = ActivityShowEditDeleteBinding.inflate(layoutInflater)
        setContentView(bindingShow.root )

        bindingShow.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        bindingShow.recyclerView.addItemDecoration(
            DividerItemDecoration(bindingShow.recyclerView.context,
                DividerItemDecoration.VERTICAL) )
    }
    override fun onResume() {
        super.onResume()
        callStudent()
    }
    fun callStudent(){
        bindingShow.edtSearch.text?.clear()
        studentList.clear()
        createClient.retrieveStudent()
            .enqueue(object : Callback<List<Student>> {
                override fun onResponse(
                    call: Call<List<Student>>,
                    response: Response<List<Student>>
                ) {
                    response.body()?.forEach {
                        studentList.add(Student(it.std_id, it.std_name, it.std_age))
                    }
                    bindingShow.recyclerView.adapter = EditStudentsAdapter(
                                                           studentList, applicationContext)
                }

                override fun onFailure(call: Call<List<Student>>, t: Throwable){
                    t.printStackTrace()
                    Toast.makeText(applicationContext,"Error2", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun clickSearch(v: View){
        studentList.clear()
        if(bindingShow.edtSearch.text!!.isEmpty()){
            callStudent()
        }else{
            createClient.retrieveStudentID(bindingShow.edtSearch.text.toString())
                .enqueue(object : Callback<Student> {
                    override fun onResponse(call: Call<Student>, response: Response<Student>) {
                        if(response.isSuccessful){
                            studentList.add(Student(response.body()?.std_id.toString(),
                                response.body()?.std_name.toString(),
                                response.body()?.std_age.toString().toInt()
                            ))
                            bindingShow.recyclerView.adapter= EditStudentsAdapter(
                                                               studentList,applicationContext)
                        }else{
                            Toast.makeText(applicationContext,"Student ID Not Found",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Student>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error onFailure " + t.message,
                            Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}
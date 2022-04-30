package com.example.android.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.android.bookhub.R
import com.example.android.bookhub.adapter.DashboardRecyclerAdapter
import com.example.android.bookhub.model.Book
import com.example.android.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class   DashboardFragment : Fragment() {


    private lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar

    var bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{ book1 , book2 ->

        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }
        else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {



            val jasonObjectRequest =
                    object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                        try{
                            progressLayout.visibility = View.GONE
                            val success = it.getBoolean("success")

                            if (success) {
                                val data = it.getJSONArray("data")
                                for (i in 0 until data.length()) {
                                    val bookJasonObject = data.getJSONObject(i)
                                    val bookObject = Book(
                                            bookJasonObject.getString("book_id"),
                                            bookJasonObject.getString("name"),
                                            bookJasonObject.getString("author"),
                                            bookJasonObject.getString("rating"),
                                            bookJasonObject.getString("price"),
                                            bookJasonObject.getString("image")

                                            )
                                    bookInfoList.add(bookObject)
                                }
                                recyclerAdapter =
                                        DashboardRecyclerAdapter(activity as Context, bookInfoList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                                recyclerDashboard.addItemDecoration(
                                        DividerItemDecoration(
                                                recyclerDashboard.context,
                                                (layoutManager as LinearLayoutManager).orientation
                                        )
                                )
                            }
                            else{
                                Toast.makeText(activity as Context,"Some Error Occure!!",Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException){
                            Toast.makeText(activity as Context, "Some unexpected error occured",Toast.LENGTH_SHORT).show()
                        }



                    },
                            Response.ErrorListener {

                                if(activity != null) {
                                    Toast.makeText(
                                            activity as Context,
                                            "Volley error Occur",
                                            Toast.LENGTH_SHORT
                                    )
                                            .show()
                                }


                            }) {

                        override fun getHeaders(): MutableMap<String, String> {
                            val header = HashMap<String, String>()
                            header["Content-type"] = "application/json"
                            header["token"] = "298e7d6b47437b"
                            return header
                        }

                    }

            queue.add(jasonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failure")
            dialog.setMessage("No InterNet Connection Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->

                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()

        }




        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}
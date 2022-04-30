package com.example.android.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bookhub.R
import com.example.android.bookhub.database.BookEntity
import com.squareup.picasso.Picasso


class FavouriteRecyclerAdapter(val context : Context, private val bookList : List<BookEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view : View):RecyclerView.ViewHolder(view){
        val textBookName : TextView = view.findViewById(R.id.txtFavBookTitle)
        val textBookAuthor : TextView=view.findViewById(R.id.txtFavBookAuthor)
        val textBookPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
        val textBookRating : TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBookCover : ImageView = view.findViewById(R.id.imgFavBookImage)
//        val llContent:LinearLayout = view.findViewById(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)

        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.textBookName.text = book.bookName
        holder.textBookAuthor.text= book.bookAuthor
        holder.textBookPrice.text = book.bookPrice
        holder.textBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web).into(holder.imgBookCover)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}
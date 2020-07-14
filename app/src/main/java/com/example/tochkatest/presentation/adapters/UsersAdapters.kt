package com.example.tochkatest.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tochkatest.R
import com.example.tochkatest.domain.models.UserDomainModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_search_user.view.*

// Адаптер работает со списком пользователей
// При достижении конца списка, срабатывает callback, который уведомляет
// и передает номер следующей страницы требующей данные
// Под страницой подразумевается порция данных. Порции состоят из 30 элементов
class UsersAdapters : RecyclerView.Adapter<UsersAdapters.ViewHolder>() {
    var currentPage = 1 // Текущая страница
    lateinit var callback: Callback
    var users = mutableListOf<UserDomainModel>()
        set(value) {
            //TODO("Неэффективное обновление списка, позже переделать")
            users.clear()
            users.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        if (position == users.size - 1) {
            // Достигнут конец списка, нужно подгрузить новые данные
            callback.onLoadUsersForNextPage(++currentPage)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val user = users[position]

            with(itemView) {
                login_text.text = resources.getString(R.string.adapter_user_name, user.login)
                id_text.text = resources.getString(R.string.adapter_user_id, user.id)

                Picasso.get()
                    .load(user.avatarUrl)
                    .error(R.drawable.ic_no_avatar)
                    .placeholder(R.drawable.ic_no_avatar)
                    .into(avatar_image)
            }
        }
    }

    interface Callback {
        // Уведомляет о достижении конца списка и передает номер следующей страницы
        // для которой нужно загрузить новую порцию данных
        fun onLoadUsersForNextPage(nextPage: Int)
    }
}
package com.example.tochkatest.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.tochkatest.domain.models.UserDomainModel

class UserDiffUtilCallback(
    private val oldList: List<UserDomainModel>,
    private val newList: List<UserDomainModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.id == newItem.id
                && oldItem.login == newItem.login
                && oldItem.avatarUrl == newItem.avatarUrl
    }
}
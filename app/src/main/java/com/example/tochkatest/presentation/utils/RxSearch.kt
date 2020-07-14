package com.example.tochkatest.presentation.utils

import androidx.appcompat.widget.SearchView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

// Создает поток данных из символов, которые ввел пользователь в строку поиска (SearchView)
class RxSearch {
    companion object {
        fun from(searchView: SearchView): Flowable<String> {
            return Flowable.create<String>({ emitter ->
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { emitter.onNext(it) }
                        return false
                    }
                })
            }, BackpressureStrategy.BUFFER)
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter { s -> s.isNotEmpty() }
                .distinctUntilChanged()
        }
    }
}
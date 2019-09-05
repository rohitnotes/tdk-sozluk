package com.mucahitkambur.tdksozluk.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.mucahitkambur.tdksozluk.R
import com.tapadoo.alerter.Alerter

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) = ViewModelProviders.of(this, provider).get(VM::class.java)

inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    provider: ViewModelProvider.Factory
) = ViewModelProviders.of(this, provider).get(VM::class.java)

fun hasNetwork(context: Context): Boolean? {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
        isConnected = true
    return isConnected
}

fun Fragment.showError(message: String?) {
    Alerter.create(activity)
        .setTitle(message ?: "")
        .setBackgroundColorRes(R.color.red)
        .show()
}

fun Fragment.findNavController(): NavController =
    androidx.navigation.fragment.NavHostFragment.findNavController(this)

fun alphabetPerCharacter(word: String): List<String> {

    val characterList: MutableList<String> = arrayListOf()

    for (character in word)
        characterList.add(character.toString())

    return characterList
}

fun Fragment.divider(): DividerItemDecoration {
    val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
    return itemDecorator
}

fun Fragment.hideKeyboard(){
    val imm: InputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = requireActivity().currentFocus

    if (view == null)
        view = View(activity)

    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
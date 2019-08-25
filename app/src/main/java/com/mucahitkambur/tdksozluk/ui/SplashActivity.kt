package com.mucahitkambur.tdksozluk.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mucahitkambur.tdksozluk.network.local.AppDatabase
import com.mucahitkambur.tdksozluk.ui.main.MainViewModel
import com.mucahitkambur.tdksozluk.util.*
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var roomDatabase: AppDatabase

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)

        if(sharedPreferences.getBoolean("is_first", true)){
            viewModel.autocomplete()
            observeAutocomp()
        }else {
            startHomeActivity()
        }
    }

    private fun observeAutocomp(){
        viewModel.autocompResult.observe(this, EventObserver { it ->
            if(it.status == Status.SUCCESS){
                it.data?.let { autocomp ->
                    appExecutors.diskIO().execute {
                        roomDatabase.autocompDao().insert(autocomp)
                    }
                    sharedPreferences.edit().putBoolean("is_first", false).apply()
                    startHomeActivity()
                }
            }else if (it.status == Status.ERROR){

            }
        })
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
package cn.foretree.samples

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cn.foretree.db.AppDatabaseManager
import cn.foretree.db.User
import com.facebook.stetho.Stetho
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscriber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)
        AppDatabaseManager.getInstance().init(this.application)
    }

    @SuppressLint("CheckResult")
    fun clickInsert(view: View) {
        getFlowable(User("谢杨学君", 1))
                .subscribe(Consumer {
                    Log.d("===>", "success  ==> $it")
                })
    }

    fun clickDelete(view: View) {

    }

    fun clickUpdate(view: View) {

    }

    fun clickQueryAll(view: View) {
        object : Flowable<List<User>>() {
            override fun subscribeActual(s: Subscriber<in List<User>>) {
                try {
                    val list = AppDatabaseManager.getInstance().getUserDao().queryAll()
                    s.onNext(list)
                } catch (e: Exception) {
                    e.printStackTrace()
                    s.onError(e)
                }
                s.onComplete()
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val fromList = JsonParserUtil.fromList(it)
                    tv_message.text = fromList
                }
    }

    fun getFlowable(user: User): Flowable<Boolean> {
        return object : Flowable<Boolean>() {
            override fun subscribeActual(s: Subscriber<in Boolean>) {
                try {
                    AppDatabaseManager.getInstance().getUserDao().insert(user)
                    s.onNext(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    s.onError(e)
                }
                s.onComplete()
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

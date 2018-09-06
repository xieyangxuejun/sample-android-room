package cn.foretree.samples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cn.foretree.db.AppDatabaseManager
import cn.foretree.db.Repo
import cn.foretree.db.User
import com.facebook.stetho.Stetho
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun clickInsert(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().run {
                getUserDao().insert(*arrayOf(
                        User("谢杨学君", 1, arrayList(), 1),
                        User("谢杨学君", 1, arrayList(), 2),
                        getUser(),
                        getUser(),
                        getUser()
                ))
                getRepoDao().insert(*arrayOf(
                        Repo("xieyangxuejun", "我是最烧的", Repo.Owner("ddd", "hhhh"),1),
                        Repo("xieyangxuejun", "我是最烧的")
                ))
            }
        }).subscribe({
            Log.d("===>", "success (size= ${it.size} and ${it.joinToString()})")
        }).isDisposed
    }

    fun clickDelete(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getUserDao().delete(*arrayOf(
                    User("谢杨学君", 1, arrayList(), 1),
                    User("谢杨学君", 1, arrayList(), 2)
            ))
        }).subscribe({
            Log.d("===>", "success (count = $it)")
        }).isDisposed
    }

    fun clickUpdate(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getUserDao().delete(*arrayOf(
                    getUser(),
                    User("谢杨学君", 2, arrayList())
            ))
        }).subscribe({
            Log.d("===>", "success (count = $it)")
        }).isDisposed
    }


    fun clickQueryAll(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().run {
                getUserDao().queryAll()
                getRepoDao().queryAll()
            }
        }).subscribe({
            val fromList = JsonParserUtil.fromList(it)
            tv_message.text = fromList
        }).isDisposed
    }

    fun <T> getFlowable(method: () -> T): Flowable<T> {
        return object : Flowable<T>() {
            override fun subscribeActual(s: Subscriber<in T>) {
                try {
                    s.onNext(method.invoke())
                } catch (e: Exception) {
                    e.printStackTrace()
                    s.onError(e)
                }
                s.onComplete()
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    private fun getUser() = User("谢杨学君", 1, arrayList())

    private fun arrayList(): ArrayList<String> {
        return arrayListOf(
                "123", "wowowo", "😆😆", "-=-=-=="
        )
    }
}

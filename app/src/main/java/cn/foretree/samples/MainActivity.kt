package cn.foretree.samples

import android.arch.paging.LivePagedListBuilder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cn.foretree.db.star.AppDatabaseManager
import cn.foretree.db.star.EDIT_DATABASE_NAME
import cn.foretree.db.star.EditData
import cn.foretree.db.star.RxJava2Helper
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

    //分页查询不行
    fun clickQueryRx(view: View) {
        RxJava2Helper.getFlowable {
            AppDatabaseManager.getInstance().getEditDataDao().pagingQuery(0).apply {
            }
        }.subscribe({
            it.create()
            val liveData = LivePagedListBuilder(it, 20).build()
            val value = liveData.value
            val toList = value?.toList()
            val fromList = JsonParserUtil.fromList(toList)
            tv_message.text = fromList
        }).isDisposed
    }

    fun clickInsert(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().run {
                getEditDataDao()?.insert(*arrayOf(
                        EditData(123, 1, "{json:123}", 1),
                        EditData(123, 1, "{json:123}", 2),
                        EditData(123, 1, "{json:123}", 3)
                ))
            }
        }).subscribe({
            Log.d("===>", "success (size= ${it?.size} and ${it?.joinToString()})")
        }).isDisposed
    }

    fun clickDelete(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getEditDataDao()?.run {
                //                delete(*arrayOf(
//                        EditData(0, 0, "",1),
//                        EditData(0, 0, "",2),
//                        EditData(0, 0, "",3)
//                ))
                delete(1, 2, 3)
            }
        }).subscribe({
            Log.d("===>", "success (count = $it)")
        }).isDisposed
    }

    fun clickUpdate(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getEditDataDao()?.update(EditData(0, 0, "", 1))
            AppDatabaseManager.getInstance().getEditDataDao()?.update(EditData(0, 0, "", 2))
        }).subscribe({
            Log.d("===>", "success (count = $it)")
        }).isDisposed
    }


    fun clickQueryAll(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getEditDataDao()?.queryAll()
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
}

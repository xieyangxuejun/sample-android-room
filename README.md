# sample-android-room
room数据库框架使用

## 基本介绍

Room中有三个主要的组件：

- **Database**:你可以用这个组件来创建一个database holder。注解定义实体的列表，类的内容定义从数据库中获取数据的对象（DAO）。它也是底层连接的主要入口。这个被注解的类是一个继承RoomDatabase的抽象类。在运行时，可以通过调用Room.databaseBuilder() 或者 Room.inMemoryDatabaseBuilder()来得到它的实例。

- **Entity**:这个组件代表一个持有数据库的一个表的类。对每一个entity，都会创建一个表来持有这些item。你必须在Database类中的entities数组中引用这些entity类。entity中的每一个field都将被持久化到数据库，除非使用了@Ignore注解。
- **DAO**:这个组件代表一个作为Data Access Objec的类或者接口。DAO是Room的主要组件，负责定义查询（添加或者删除等）数据库的方法。使用@Database注解的类必须包含一个0参数的，返回类型为@Dao注解过的类的抽象方法。Room会在编译时生成这个类的实现。

![](room_architecture.png)

## 主要注解使用

- @Entity, 将这个类转换成数据表, indices表指数, primaryKeys主键[]数组类型,和其他
- @Igore,如果不喜欢存数据

```
@Entity(tableName = "user")
data class User(
        val name: String,
        val sex: Int,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val createAt: Long = System.currentTimeMillis()
)
```

- @Dao, 数据操作接口.

```
@Dao
interface UserDao {
    //多个数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User): List<Long>

    @Delete
    fun delete(vararg user: User): Int

    @Update
    fun update(vararg user: User): Int

    @Query("SELECT * FROM user")
    fun queryAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIDs)")
    fun queryByUserIds(vararg userIDs: Long): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun queryById(id: Long): User
}
```

- @Database, 新建数据表在这里添加,和版本号,如果数据改变一定要改变版本号

```
@Database(entities = arrayOf(
        User::class
), version = databaseVersion)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}
```

- 创建, 使用单例,这样不用每次创建了,但是room给了一方法==>Room.inMemoryDatabaseBuilder(context, User::class.java)

```
@SuppressLint("StaticFieldLeak")
class AppDatabaseManager {

    private var mContext: Context? = null
    private var mAppDatabase: AppDatabase? = null

    companion object {
        @Volatile
        private var mInstance: AppDatabaseManager? = null

        fun getInstance(): AppDatabaseManager {
            if (mInstance == null) {
                synchronized(AppDatabaseManager::class.java) {
                    if (mInstance == null) {
                        mInstance = AppDatabaseManager()
                    }
                }
            }
            return mInstance!!
        }
    }

    fun init(app: Application) {
        mInstance?.run {
            if (mContext == null) {
                mContext = app.applicationContext
            }
            mAppDatabase = Room.databaseBuilder(
                    mContext!!, AppDatabase::class.java, databaseName
            ).build()
        }
    }


    fun getUserDao(): UserDao = mAppDatabase?.getUserDao()!!
}
```

## 注意

- 如果id自增, 需要放在data class 后面赋值为0, ~~网上说需要无参数的构造函数,不需要~~
- 注解@field: SerializedName("user_name")和@ColumnInfo(name = "user_name"),前者是序列化的变量名,后者是数据表的变量名.如果不写就用类成员变量名.
- **重点**:不要在主线程操作数据(CRUD).....
- Insert插入多个数据返回的是List<Long> ids
- delete也有返回值是Int,删除的个数, 同update
- 查询就有很多了..order..like..=..

## 使用

- 初始化

```
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)
        AppDatabaseManager.getInstance().init(this.application)
}
```

- 增删改查

```
fun clickInsert(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getUserDao().insert(*arrayOf(
                    User("谢杨学君", 1, 1),
                    User("谢杨学君", 1, 2),
                    User("谢杨学君", 1),
                    User("谢杨学君", 1),
                    User("谢杨学君", 1)
            ))
        }).subscribe({
            Log.d("===>", "success (size= ${it.size} and ${it.joinToString()})")
        }).isDisposed
    }

    fun clickDelete(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getUserDao().delete(*arrayOf(
                    User("谢杨学君", 1, 1),
                    User("谢杨学君", 1, 2)
            ))
        }).subscribe({
            Log.d("===>", "success (count = $it)")
        }).isDisposed
    }

    fun clickUpdate(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getUserDao().delete(*arrayOf(
                    User("谢杨学君", 1),
                    User("谢杨学君", 2)
            ))
        }).subscribe({
            Log.d("===>", "success (count = $it)")
        }).isDisposed
    }

    fun clickQueryAll(view: View) {
        getFlowable({
            AppDatabaseManager.getInstance().getUserDao().queryAll()
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
```



## 总结一下

可以当成单独的模块来使用, fork然后写自己的Entities就行了.
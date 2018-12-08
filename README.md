 
## AdvanceRecyclerView




 ### Video Demo
![video demo](https://github.com/RezaMasoudi/AdvanceRecyclerView/raw/master/demo.gif "Video Demo")


--
 
 ### Options
  + Swipe Refresh
  + Content Refresh
  + Content Message
  + Endless List
  + Footer Loading
  + Swipe to Dismiss



---


### Quick Setup

***add list in layout***

```xml

    <ir.ncbox.libarary.AdvanceRecyclerView
        android:id="@+id/rv_advance_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

```

***in Activity***

```kotlin

    val layoutManager = LinearLayoutManager(this)
    val adapter = UsersAdapter(this)
    rv_advance_list.setLayoutManager(layoutManager)
    rv_advance_list.setAdapter(adapter)

```

---

### Swipe Refresh

When list pull down showing Swipe Refresh and you can call API in ```onRefresh listener```

```kotlin

class MainActivity : Activity(), OnRefreshListener{

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        rv_advance_list.onRefreshListener = this

    }
    
    // on page refresh
    override fun onRefresh() {
       // call api
       getUserFromAPI()
    }

}

```

So, for cancel Swipe Refresh call ```cancelRefreshing``` Method
```kotlin
rv_advance_list.cancelRefreshing()

```

and for disable swipe refresh use
```kotlin
 rv_advance_list.setEnableRefreshing(false)
````

## Content Refreshing

well, for show content progressBar call showContentLoading
```kotlin
   rv_advance_list.showContentLoading()
```
and afetr content loaded show list by call showList Method
```kotlin
   rv_advance_list.showList()
```

## Content Message

SomeTimes we want show message like network error Or empty result message, you use this method:
```kotlin
  rv_advance_list.showContentMessage(resources.getString(R.string.network_error), R.drawable.ic_wifi_grey_600_48dp)
```
## Endless List

for use Infinit List mode first add end page listener for detect page scroll end
```kotlin

class MainActivity : Activity(), OnScrollEndListener {

 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // add endless listener
        rv_advance_list.scrollEndListener = this
    }

       // on page scroll end
    override fun onEndPage() {
      // wait listener unti data receive
      rv_advance_list.pauseEndlessListener()
      
      // call api
      getUsersFromAPI()
    
    }
    
     override fun onDataReceived() {
       // after data received start listener by call this method
      rv_advance_list.startEndlessListener()
      
    }

}

```

## Footer Loading

well, When list scroll end showing footer loading is awesome
so, for use this option your adapter must be extends AdvanceRecyclerViewAdapter

Example:

```kotlin

class UsersAdapter(context: Context) : AdvanceRecyclerViewAdapter<UserModel, UsersAdapter.UserHolder>(context) {

    override fun onCreateDataItemViewHolder(parent: ViewGroup?, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.user_item_view, parent, false)
        return UserHolder(view)
    }

    override fun onBindDataItemViewHolder(holder: UserHolder?, position: Int) {
        holder!!.bindItem(data[position])
    }

    override fun footerOnVisibleItem() {

    }


    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(user: User) {
            itemView.tv_user_item_username.text = user.name
            itemView.tv_user_item_job.text = user.job
        }

    }

}

````

for show footer loading first create loading layout xml here is sample

footer_loadin.xml
```xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:paddingTop="20dp"
    android:paddingBottom="20dp"
    android:layout_height="wrap_content"
    android:gravity="center">

    <ProgressBar
        android:layout_width="wrap_content"
        android:indeterminateDuration="1000"
        android:layout_height="wrap_content"/>


</LinearLayout>

```

Ok Next, in Activity define layout as footer loading view
```kotlin
class MainActivity : Activity(), OnScrollEndListener {

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // define footer layout
        rv_advance_list.setFooterLoadingView(R.layout.footer_loading)
        
    }
    
     // on page scroll end
    override fun onEndPage() {
       // wait listener unti data receive
      rv_advance_list.pauseEndlessListener()
      
      // show footer loading
      rv_advance_list.showFooterLoading()
      
      
    }
    
     override fun onDataReceived() {
       // after data received start listener by call this method
      rv_advance_list.startEndlessListener()
      
      // cancel footer loading
      rv_advance_list.cancelFooterLoading()
      
      
    }


}

```

## Swipe To Dismiss

For Example, you wand delete use via Swipe lisy item to sides lets go do it

```kotlin

class MainActivity : Activity(), SwipeToDismiss.SwipetoDismissCallBack {

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // swipe to delete user first argument is icon and argument two is color dismiss
         rv_advance_list.setToRightSwipeItem(R.drawable.ic_delete_grey_600_36dp,
                ContextCompat.getColor(this, R.color.red_light), this)
        
    }
    
    override fun onSwipedToRight(viewHolder: RecyclerView.ViewHolder) {
       
       // remove item from list
    }

    override fun onSwipedToLeft(viewHolder: RecyclerView.ViewHolder) {
    }
    
    
  }

```





### APK Demo
[Download](https://github.com/RezaMasoudi/AdvanceRecyclerView/raw/master/demo.apk)













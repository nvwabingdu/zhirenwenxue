package cn.dreamfruits.yaoguo.view.dialog


import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import cn.dreamfruits.yaoguo.R
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

/**
 * 一键绑定手机号
 */
class OneKeyBindPhoneDialog(
    context: Context,
    theme: Int = 0
) : AppCompatDialog(context, theme) {

    var mCloseIc: ImageView? = null
    var mPhoneNumberTv: TextView? = null
    var mSloganTv: TextView? = null
    var mBindPhone: TextView? = null
    var mBindOtherPhone: TextView? = null


    init {

        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            decorView.setPadding(0,0,0,0)
            setWindowAnimations(R.style.BottomDialogAnimation)
        }

        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = MATCH_PARENT

        initViews()
    }


    private fun initViews() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_onekey_bind_phone, null)
        setContentView(view)

        mCloseIc = view.findViewById(R.id.ic_close)
        mPhoneNumberTv = view.findViewById(R.id.tv_phone_number)
        mSloganTv = view.findViewById(R.id.tv_bind_phone_slogan)
        mBindPhone = view.findViewById(R.id.tv_bind_phone)
        mBindOtherPhone = view.findViewById(R.id.tv_bind_other_phone)

    }


    class Builder(val context: Context) {
        var mPhoneNumber: String? = null
        var mSlogan: String? = null
        var mOnBindPhoneListener: OnBindPhoneListener? = null
        var mOnCloseListener: OnCloseListener? = null
        var mOnBindOtherPhoneListener: OnBindOtherPhoneListener? = null
        var mCancelable: Boolean = true


        fun setPhoneNumber(phoneNumber: String): Builder {
            this.mPhoneNumber = phoneNumber
            return this
        }

        fun setSlogan(slogan: String): Builder {
            this.mSlogan = slogan
            return this
        }


        fun setOnBindPhoneClick(bindPhoneListener: OnBindPhoneListener): Builder {
            this.mOnBindPhoneListener = bindPhoneListener
            return this
        }

        fun setOnCloseListener(closeListener: OnCloseListener): Builder {
            this.mOnCloseListener = closeListener
            return this
        }

        fun setOnBindOtherPhoneListener(bindOtherPhoneListener: OnBindOtherPhoneListener): Builder {
            this.mOnBindOtherPhoneListener = bindOtherPhoneListener
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.mCancelable = cancelable
            return this
        }

        fun build(): OneKeyBindPhoneDialog {
            return OneKeyBindPhoneDialog(context).apply {
                setCanceledOnTouchOutside(mCancelable)
                setOnDismissListener {
                    mOnCloseListener?.onClick()
                }

                mPhoneNumber?.let {
                    mPhoneNumberTv?.text = it
                }
                mSlogan?.let {
                    mSloganTv?.text = it
                }

                mCloseIc?.setOnClickListener {
                    dismiss()
                }

                mBindPhone?.setOnClickListener {
                    dismiss()
                    mOnBindPhoneListener?.onClick()
                }

                mBindOtherPhone?.setOnClickListener {
                    dismiss()
                    mOnBindOtherPhoneListener?.onClick()
                }
            }
        }
    }


    fun interface OnBindPhoneListener {
        fun onClick()
    }

    fun interface OnCloseListener {
        fun onClick()
    }

    fun interface OnBindOtherPhoneListener {
        fun onClick()
    }

}



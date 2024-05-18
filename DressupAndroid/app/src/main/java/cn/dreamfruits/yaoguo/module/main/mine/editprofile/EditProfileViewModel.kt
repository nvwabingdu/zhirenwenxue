package cn.dreamfruits.yaoguo.module.main.mine.editprofile

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.repository.UserRepository
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMSDKListener
import com.tencent.imsdk.v2.V2TIMUserFullInfo
import org.koin.core.component.inject


/***
 * 编辑资料的ViewModel
 */
class EditProfileViewModel : BaseViewModel() {

    val userRepository by inject<UserRepository>()

    private val _editProfileState = MutableLiveData<ProfileState>()
    val editProfileState: LiveData<ProfileState> get() = _editProfileState


    /**
     * 修改名称
     */
    fun updateNickName(name: String) {
        val disposable = userRepository.updateUserInfo(nickName = name)
            .subscribe({
                _editProfileState.value = ProfileState.Success(it)
            }, {
                _editProfileState.value = ProfileState.Fail
            })

    }

    /**
     * 修改简介
     */
    fun updateDescription(description: String) {
        val disposable = userRepository.updateUserInfo(description = description)
            .subscribe({
                _editProfileState.value = ProfileState.Success(it)
            }, {
                _editProfileState.value = ProfileState.Fail
            })

    }


    /**
     * 修改微酸号
     */
    fun updateYgId(ygId: String) {
        val disposable = userRepository.updateUserInfo(ygId = ygId)
            .subscribe({
                _editProfileState.value = ProfileState.Success(it)
            }, {
                _editProfileState.value = ProfileState.Fail
            })

    }


    /**
     * 修改生日
     */
    fun updateBirthday(birthday: Long?) {
        val disposable = userRepository.updateUserInfo(birthday = birthday)
            .subscribe({
                _editProfileState.value = ProfileState.Success(it)
            }, {
                _editProfileState.value = ProfileState.Fail
            })

    }

    /**
     * 修改位置
     */
    fun updateLocation(country: String?, province: String?, city: String?) {
        val disposable =
            userRepository.updateUserInfo(country = country, province = province, city = city)
                .subscribe({
                    _editProfileState.value = ProfileState.Success(it)
                }, {
                    _editProfileState.value = ProfileState.Fail
                })
    }

    /**
     * 修改性别
     */
    fun updateGender(gender: Int) {
        val disposable = userRepository.updateUserInfo(gender)
            .subscribe({
                _editProfileState.value = ProfileState.Success(it)
            }, {
                _editProfileState.value = ProfileState.Fail
            })

    }


}
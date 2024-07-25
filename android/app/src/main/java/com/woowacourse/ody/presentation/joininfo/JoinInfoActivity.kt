package com.woowacourse.ody.presentation.joininfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.woowacourse.ody.databinding.ActivityJoinInfoBinding
import com.woowacourse.ody.presentation.adapter.InfoViewPagerAdapter
import com.woowacourse.ody.presentation.completion.JoinCompleteActivity
import com.woowacourse.ody.presentation.meetinginfo.BackListener
import com.woowacourse.ody.presentation.meetinginfo.MeetingInfoViewModel
import com.woowacourse.ody.presentation.nickname.JoinNickNameFragment
import com.woowacourse.ody.presentation.startingpoint.JoinStartingPointFragment
import com.woowacourse.ody.util.NextListener

class JoinInfoActivity : AppCompatActivity(), NextListener, BackListener {
    private val binding: ActivityJoinInfoBinding by lazy {
        ActivityJoinInfoBinding.inflate(layoutInflater)
    }
    private val viewModel: MeetingInfoViewModel by viewModels<MeetingInfoViewModel>()
    private val fragments: List<Fragment> by lazy {
        listOf(JoinNickNameFragment(), JoinStartingPointFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initializeDataBinding()
    }

    private fun initializeDataBinding() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.nextListener = this
        binding.backListener = this
        initializeJoinInfoViewPager()
    }

    private fun initializeJoinInfoViewPager() {
        val visitorOnBodingInfoAdapter: InfoViewPagerAdapter =
            InfoViewPagerAdapter(this, fragments)

        binding.vpJoinInfo.adapter = visitorOnBodingInfoAdapter
        binding.wdJoinInfo.attachTo(binding.vpJoinInfo)
    }

    override fun onNext() {
        if (binding.vpJoinInfo.currentItem == fragments.size - 1) {
            val joinInfo =
                arrayListOf(
                    "inviteCode",
                    viewModel.nickname.value.toString(),
                    viewModel.startingPointGeoLocation.value!!.address,
                    viewModel.startingPointGeoLocation.value!!.latitude,
                    viewModel.startingPointGeoLocation.value!!.longitude,
                )

            startActivity(JoinCompleteActivity.getJoinInfoIntent(this, joinInfo))
            finish()
            return
        }
        binding.vpJoinInfo.currentItem += 1
    }

    override fun onBack() {
        if (binding.vpJoinInfo.currentItem > 0) {
            binding.vpJoinInfo.currentItem -= 1
            return
        }
        finish()
    }

    private fun getInviteCode(): String? = intent.getStringExtra(INVITE_CODE_KEY)

    companion object {
        private const val INVITE_CODE_KEY = "invite_code_key"

        fun getIntent(
            inviteCode: String,
            context: Context,
        ): Intent {
            return Intent(context, JoinInfoActivity::class.java).also {
                it.putExtra(inviteCode, INVITE_CODE_KEY)
            }
        }
    }
}

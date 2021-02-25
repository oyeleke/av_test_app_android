/**
 * Copyright (c) 2019 Cotta & Cush Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avtestapp.android.androidbase.di

import android.app.Application
import com.avtestapp.android.androidbase.MainActivity
import com.avtestapp.android.androidbase.av_test.auth.forgot_password.ForgotPasswordFragment
import com.avtestapp.android.androidbase.av_test.auth.login.LoginFragment
import com.avtestapp.android.androidbase.av_test.auth.reset_password.ResetPasswordFragment
import com.avtestapp.android.androidbase.av_test.auth.sign_up.SignUpFragment
import com.avtestapp.android.androidbase.av_test.auth.splash.SplashScreenFragment
import com.avtestapp.android.androidbase.av_test.auth.verification.VerificationFragment
import com.avtestapp.android.androidbase.av_test.core.DashboardFragment
import com.avtestapp.android.androidbase.av_test.core.change_password.ChangePasswordFragment
import com.avtestapp.android.androidbase.av_test.core.edit_profile.EditProfileFragment
import com.avtestapp.android.androidbase.av_test.core.knowledge_score.KnowledgeScoreFragment
import com.avtestapp.android.androidbase.av_test.core.marked_question.MarkedPracticeQuestionFragment
import com.avtestapp.android.androidbase.av_test.core.marked_question.MarkedQuestionsFragment
import com.avtestapp.android.androidbase.av_test.core.marked_question.MarkedStudyQuestionFragment
import com.avtestapp.android.androidbase.av_test.core.questions.*
import com.avtestapp.android.androidbase.av_test.core.settings.SettingsFragment
import com.avtestapp.android.androidbase.av_test.modals.QuestionTypeBottomSheetModal
import com.avtestapp.android.androidbase.av_test.onboarding.Onboarding3Fragment
import com.avtestapp.android.androidbase.av_test.onboarding.OnboardingStartFragment
import com.avtestapp.android.androidbase.av_test.onboarding.upload_image.UploadImageFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.BindsInstance
import dagger.Component
import java.sql.ResultSetMetaData
import javax.inject.Singleton

@Singleton
@Component(modules = [APIServiceModule::class, ViewModelModule::class, LocalDataModule::class])
interface AppComponent {

    fun inject(target: MainActivity)
    fun inject(target: LoginFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: VerificationFragment)
    fun inject(target: ResetPasswordFragment)
    fun inject(target: ForgotPasswordFragment)
    fun inject(target: UploadImageFragment)
    fun inject(target: OnboardingStartFragment)
    fun inject(target: Onboarding3Fragment)
    fun inject(target: QuestionTypeFragment)
    fun inject(target: StudyQuestionFragment)
    fun inject(target: PracticeQuestionsFragment)
    fun inject(target: QuestionTypeBottomSheetModal)
    fun inject(target: DashboardFragment)
    fun inject(target: SettingsFragment)
    fun inject(target: EditProfileFragment)
    fun inject(target: SplashScreenFragment)
    fun inject(target: KnowledgeScoreFragment)
    fun inject(target: ReviewQuestionsFragment)
    fun inject(target: MarkedQuestionsFragment)
    fun inject(target: MarkedPracticeQuestionFragment)
    fun inject(target: MarkedStudyQuestionFragment)
    fun inject(target: ChangePasswordFragment)



    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}

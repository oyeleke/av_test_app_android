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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avtestapp.android.androidbase.av_test.auth.forgot_password.ForgotPasswordViewModel
import com.avtestapp.android.androidbase.av_test.auth.login.LoginViewModel
import com.avtestapp.android.androidbase.av_test.auth.reset_password.ResetPasswordViewModel
import com.avtestapp.android.androidbase.av_test.auth.sign_up.SignUpViewModel
import com.avtestapp.android.androidbase.av_test.auth.verification.VerificationViewModel
import com.avtestapp.android.androidbase.av_test.core.DashboardViewModel
import com.avtestapp.android.androidbase.av_test.core.edit_profile.EditProfileViewModel
import com.avtestapp.android.androidbase.av_test.core.knowledge_score.KnowledgeScoreViewModel
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionDialogViewModel
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionViewModel
import com.avtestapp.android.androidbase.av_test.core.settings.SettingsViewModel
import com.avtestapp.android.androidbase.av_test.onboarding.Onboarding3ViewModel
import com.avtestapp.android.androidbase.av_test.onboarding.upload_image.UploadImageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AvTestAppViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerificationViewModel::class)
    abstract fun bindVerificationViewModel(viewModel: VerificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun bindResetPasswordViewModel(viewModel: ResetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UploadImageViewModel::class)
    abstract fun bindsUploadImageViewModel(viewModel: UploadImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(Onboarding3ViewModel::class)
    abstract fun bindsOnboarding3ViewModel(viewModel: Onboarding3ViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionViewModel::class)
    abstract fun bindsQuestionViewModel(viewModel: QuestionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindsDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionDialogViewModel::class)
    abstract fun bindsQuestionDialogViewModel(viewModel: QuestionDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindsEditProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(KnowledgeScoreViewModel::class)
    abstract fun bindsKnowledgeScoreViewModel(viewModel: KnowledgeScoreViewModel): ViewModel




    // TODO Add other ViewModels.
}

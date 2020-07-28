package dk.adaptmobile.android_seed.base

import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseTab

import dk.adaptmobile.android_seed.screens.firstview.FirstView
import dk.adaptmobile.android_seed.screens.thirdview.ThirdView
import dk.adaptmobile.android_seed.screens.fourthview.FourthView
import dk.adaptmobile.android_seed.screens.secondview.SecondView


object FirstTab : BaseTab(R.id.tab_first, ::FirstView)
object SecondTab : BaseTab(R.id.tab_second, ::SecondView)
object ThirdTab : BaseTab(R.id.tab_third, ::ThirdView)
object FourthTab : BaseTab(R.id.tab_fourth, ::FourthView)

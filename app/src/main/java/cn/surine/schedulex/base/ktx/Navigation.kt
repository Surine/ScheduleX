package cn.surine.schedulex.base.ktx

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/29/20 11:29
 */

/**
 * open a navigation page
 * */
fun Fragment.open(id: Int, bundle: Bundle? = null) = NavHostFragment.findNavController(this).navigate(id, bundle)

/**
 * close a navigation page
 * */
fun Fragment.close() = NavHostFragment.findNavController(this).navigateUp()


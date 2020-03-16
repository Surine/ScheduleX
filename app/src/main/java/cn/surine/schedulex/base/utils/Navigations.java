package cn.surine.schedulex.base.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-22 15:42
 */
public class Navigations {

    public static void open(Fragment fragment, int id) {
        NavHostFragment.findNavController(fragment).navigate(id);
    }

    public static void open(Fragment fragment, int id, Bundle bundle) {
        NavHostFragment.findNavController(fragment).navigate(id, bundle);
    }

    public static void close(Fragment fragment) {
        NavHostFragment.findNavController(fragment).navigateUp();

    }
}

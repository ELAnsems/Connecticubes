package com.mygdx.game.Algorithms.util;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages
{
    private static Map<String, ResourceBundle> bundles;

    private Messages()
    {
        // No implementation needed
    }

    public static String getMessage(final String bundleName, final String key)
    {
        String string = "";
        try
        {
            final ResourceBundle bundle = getBundle(bundleName);
            string = bundle.getString(key);
        }
        catch (final MissingResourceException e)
        {
            string = '!' + key + '!';
        }
        return string;
    }

    private static ResourceBundle getBundle(final String bundleName)
    {
        initBundles();
        return getBundleFromBundles(bundleName);
    }

    private static void initBundles()
    {
        synchronized (new Object())
        {
            if (bundles == null)
            {
                bundles = new HashMap<String, ResourceBundle>();
            }
        }
    }

    private static ResourceBundle getBundleFromBundles(final String bundleName)
    {
        ResourceBundle bundle = bundles.get(bundleName);
        if (bundle == null)
        {
            bundle = ResourceBundle.getBundle(bundleName);
            bundles.put(bundleName, bundle);
        }
        return bundle;
    }
}

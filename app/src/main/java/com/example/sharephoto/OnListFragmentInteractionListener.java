package com.example.sharephoto;

import com.example.sharephoto.dummy.PhotoContent;

/**
 * Created by Владислав on 23.01.2017.
 */

public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PhotoContent.PhotoItem item);
        void onListFragmentDelete(PhotoContent.PhotoItem item);
}

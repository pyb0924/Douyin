package io.bcyl.douyin.Fragment.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.bcyl.douyin.R;
import io.bcyl.douyin.Utils.Network;
import io.bcyl.douyin.Model.VideoInfo;

import static android.os.Looper.getMainLooper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getName();
    private LinearLayoutManager layoutManager;
    private PagerSnapHelper snapHelper;
    private static final String ARG_PARAM = "param";
    List<VideoInfo> vl = new ArrayList<>();
    private View view;
    private View v;
    private int pos;

    /**
     * For now we are using sentinel data
     */
    RecyclerView mRecyclerView;
    VideoAdapter mAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        /*
         * For now we are using sentinel data
         */
//        VideoItem src1=new VideoItem(getString(R.string.meme1),"Hugh Jaz 1","Shiba");
//        VideoItem src2=new VideoItem(getString(R.string.meme2),"Hugh Jaz 2","Birdie");
//        VideoItem src3=new VideoItem(getString(R.string.meme3),"Hugh Jaz 3","Horses in Walmart");
//        VideoItem src4=new VideoItem(getString(R.string.meme4),"Hugh Jaz 4","Sad Dog");
//        VideoItem src5=new VideoItem(getString(R.string.meme5),"Hugh Jaz 5","Omegle");
//        VideoItem src6=new VideoItem(getString(R.string.meme6),"Hugh Jaz 6","Dank cat");
//        VideoItem src7=new VideoItem(getString(R.string.meme7),"Hugh Jaz 7","Watch it!");
//        VideoItem src8=new VideoItem(getString(R.string.meme8),"Hugh Jaz 8","War face");
//
//        videoList.add(src1);
//        videoList.add(src8);
//        videoList.add(src7);
//        videoList.add(src4);
//        videoList.add(src5);
//        videoList.add(src2);
//        videoList.add(src3);
//        videoList.add(src6);
//        videoList.add(src1);
//        videoList.add(src2);
//        videoList.add(src3);
//        videoList.add(src4);
//        videoList.add(src5);
//        videoList.add(src6);
//        videoList.add(src7);
    }

    public void onStart(){
        super.onStart();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdapter = new VideoAdapter((ArrayList<VideoInfo>) vl, getActivity());
        view = inflater.inflate(R.layout.fragment_home, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = view.findViewById(R.id.rvVid);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerView(mRecyclerView);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder instanceof VideoViewHolder)
                    ((VideoViewHolder) viewHolder).getPlayer().play();
            }
        },200);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                v = snapHelper.findSnapView(layoutManager);
                pos = layoutManager.getPosition(v);
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
                if (viewHolder instanceof VideoViewHolder)
                    ((VideoViewHolder) viewHolder).getPlayer().play();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx/10, dy/10);
            }
        });
        return view;
    }

    public void pauseCurrentVideo(){
        v = snapHelper.findSnapView(layoutManager);
        pos = layoutManager.getPosition(v);
        RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
        ((VideoViewHolder) viewHolder).getPlayer().pause();
    }

    public void resumeCurrentVideo(){
        v = snapHelper.findSnapView(layoutManager);
        if (v != null) {
            pos = layoutManager.getPosition(v);
            RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
            ((VideoViewHolder) viewHolder).getPlayer().play();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"On resume");
        resumeCurrentVideo();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"On pause");
        pauseCurrentVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"On stop");
        pauseCurrentVideo();
    }

    private void getData(String usrName){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                List<VideoInfo>
                vl = Network.dataGetFromRemote(usrName);
                if (vl != null && !vl.isEmpty()){
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setData(vl);
                        }
                    });
                }
            }
        }).start();
    }

    private void getData(){
        getData(null);
    }



}
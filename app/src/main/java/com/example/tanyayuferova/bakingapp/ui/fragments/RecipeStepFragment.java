package com.example.tanyayuferova.bakingapp.ui.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.databinding.FragmentRecipeStepBinding;
import com.example.tanyayuferova.bakingapp.databinding.FragmentStepsBinding;
import com.example.tanyayuferova.bakingapp.entity.Step;
import com.example.tanyayuferova.bakingapp.utils.BindingAdaptersUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanya Yuferova on 11/13/2017.
 */

public class RecipeStepFragment extends Fragment
        implements ViewPager.OnPageChangeListener, ExoPlayer.EventListener  {

    private FragmentRecipeStepBinding binding;
    private StepDescriptionPagerAdapter stepDescriptionPagerAdapter;
    private List<Step> steps;
    private long playerPosition = 0;

    private static MediaSessionCompat mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Dialog mFullScreenDialog;

    public static final String ARGUMENT_STEPS = "arg.steps";
    public static final String ARGUMENT_SELECTED_INDEX = "arg.selected_index";
    public static final String STATE_SELECTED_INDEX = "state.selected_index";
    public static final String STATE_PLAYER_POSITION = "state.player_position";
    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private OnPageSelectedCallBack onPageSelectedCallBack;

    public interface OnPageSelectedCallBack {
        void onPageSelected(int position);
    }

    public RecipeStepFragment() {
    }

    public static RecipeStepFragment newInstance(List<Step> steps, int selectedIndex) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(new Bundle());
        fragment.getArguments().putParcelableArrayList(ARGUMENT_STEPS, new ArrayList<Parcelable>(steps));
        fragment.getArguments().putInt(ARGUMENT_SELECTED_INDEX, selectedIndex);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onPageSelectedCallBack = (OnPageSelectedCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnPageSelectedCallBack");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int selectedIndex = savedInstanceState == null ? getArguments().getInt(ARGUMENT_SELECTED_INDEX)
                : savedInstanceState.getInt(STATE_SELECTED_INDEX);
        playerPosition = savedInstanceState != null && savedInstanceState.containsKey(STATE_PLAYER_POSITION) ?
                savedInstanceState.getLong(STATE_PLAYER_POSITION) : 0;

        binding = FragmentRecipeStepBinding.inflate(inflater, container, false);
        steps = getArguments().getParcelableArrayList(ARGUMENT_STEPS);

        stepDescriptionPagerAdapter = new StepDescriptionPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(stepDescriptionPagerAdapter);
        binding.viewPager.addOnPageChangeListener(this);
        setViewPagerPosition(selectedIndex);

        View.OnClickListener backNavigation = new OnNavigationBackClickListener();
        View.OnClickListener forwardNavigation = new OnNavigationForwardClickListener();
        binding.tvBack.setOnClickListener(backNavigation);
        binding.ivBack.setOnClickListener(backNavigation);
        binding.tvNext.setOnClickListener(forwardNavigation);
        binding.ivNext.setOnClickListener(forwardNavigation);

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState==null)
            outState = new Bundle();
        outState.putInt(STATE_SELECTED_INDEX, binding.viewPager.getCurrentItem());
        if(mExoPlayer != null)
            outState.putLong(STATE_PLAYER_POSITION, mExoPlayer.getCurrentPosition());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Show video dialog if orientation is landscape and this is not screen for tablet
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !getResources().getBoolean(R.bool.isTablet)) {
            openFullscreenDialog(binding.visualResource);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * A Step description fragment for view page
     */
    public static class StepDescriptionFragment extends Fragment {

        public FragmentStepsBinding binding;
        private static final String ARG_STEP_ITEM = "step_item";


        public StepDescriptionFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StepDescriptionFragment newInstance(Step item) {
            StepDescriptionFragment fragment = new StepDescriptionFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_STEP_ITEM, item);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            binding = FragmentStepsBinding.inflate(inflater, container, false);
            Step item = getArguments().getParcelable(ARG_STEP_ITEM);
            binding.setStep(item);
            return binding.getRoot();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class StepDescriptionPagerAdapter extends FragmentPagerAdapter  {

        public StepDescriptionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return StepDescriptionFragment.newInstance(steps.get(position));
        }

        @Override
        public int getCount() {
            return steps.size();
        }
    }

    public void setViewPagerPosition(int position) {
        binding.viewPager.setCurrentItem(position);
        // For the first page onPageSelected is not called
        if(position == 0)
            onPageSelected(position);
    }

    @Override
    public void onPageSelected(int position) {
        Step step = steps.get(position);
        String videoResource = step.getVideoResource();
        String imageResource = step.getImageResource();

        // Set player for current step
        destroyPlayer();
        setupPlayer(videoResource);

        // Set image fro current step
        if(imageResource != null)
            Picasso.with(getContext()).load(imageResource).into(binding.ivStepImage);

        // Hide player and image view if there is no visual resource
        BindingAdaptersUtils.setConstraintGuidePercent(binding.horizontalHalf,
                 videoResource == null && imageResource == null ? 0.0f : 0.5f);
        binding.playerView.setVisibility(videoResource == null ? View.INVISIBLE : View.VISIBLE);
        binding.ivStepImage.setVisibility(videoResource != null || imageResource == null ? View.INVISIBLE : View.VISIBLE);

        // Setup navigation buttons
        setGoBackVisible(canGoBack() ? View.VISIBLE : View.INVISIBLE);
        setGoNextVisible(canGoNext() ? View.VISIBLE : View.INVISIBLE);

        //Notify activity that page has been changed
        onPageSelectedCallBack.onPageSelected(position);
    }

    /**
     * Inits player for current step id there is visual resource or destroy player if not
     * @param videoUrl
     */
    private void setupPlayer(String videoUrl) {
        if(videoUrl != null && !videoUrl.isEmpty()) {
            initializeMediaSession();
            initializePlayer(Uri.parse(videoUrl));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new RecipeStepFragment.MySessionCallback());
        mMediaSession.setActive(true);
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        binding.playerView.setPlayer(mExoPlayer);

        mExoPlayer.addListener(this);

        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.seekTo(playerPosition);
        playerPosition = 0;
    }

    /**
     * Destroy player
     */
    protected void destroyPlayer() {
        releasePlayer();
        if(mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null)
            playerPosition = mExoPlayer.getCurrentPosition();
        destroyPlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mExoPlayer == null) {
            Step step = steps.get(binding.viewPager.getCurrentItem());
            setupPlayer(step.getVideoResource());
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    /**
     * Shows view in full screen dialog
     * @param view
     */
    private void openFullscreenDialog(View view) {
        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ((ViewGroup) view.getParent()).removeView(view);
        mFullScreenDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenDialog.show();
    }

    /**
     * Navigates view pager to next page if possible
     * @param view
     */
    public void moveNextOnClick(View view) {
        if(canGoNext()) {
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1, true);
        }
    }

    /**
     * Navigates view pager to previous page if possible
     * @param view
     */
    public void moveBackOnClick(View view) {
        if(canGoBack()) {
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1, true);
        }
    }

    /**
     * Set back button visibility
     * @param visibility
     */
    private void setGoBackVisible(int visibility) {
        binding.ivBack.setVisibility(visibility);
        binding.tvBack.setVisibility(visibility);
    }

    /**
     * Set next button visibility
     * @param visibility
     */
    private void setGoNextVisible(int visibility) {
        binding.ivNext.setVisibility(visibility);
        binding.tvNext.setVisibility(visibility);
    }

    /**
     * Returns if there is any pages after current
     * @return
     */
    private boolean canGoNext(){
        return binding.viewPager.getCurrentItem() != steps.size()-1;
    }

    /**
     * Returns if there is any pages before current
     * @return
     */
    private boolean canGoBack() {
        return binding.viewPager.getCurrentItem() != 0;
    }

    protected class OnNavigationForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            moveNextOnClick(v);
        }
    }

    protected class OnNavigationBackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            moveBackOnClick(v);
        }
    }

}

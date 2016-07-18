package com.fanwe.library.mediaplayer;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public class SDMediaPlayer
{
	private MediaPlayer mPlayer;
	private EnumState mState = EnumState.Idle;

	private SDMediaPlayer()
	{
		mPlayer = new MediaPlayer();
		mPlayer.setOnErrorListener(mListenerOnError);
		mPlayer.setOnPreparedListener(mListenerOnPrepared);
		mPlayer.setOnCompletionListener(mListenerOnCompletion);
	}

	protected void resetBeforePlayDataSource()
	{
		stop();
		resetPlayer();
	}

	public void playUrl(String url)
	{
		resetBeforePlayDataSource();
		url = beforeSetDataSourceUrl(url);
		try
		{
			mPlayer.setDataSource(url);
			start();
		} catch (Exception e)
		{
			notifyException(e);
		}
	}

	protected String beforeSetDataSourceUrl(String url)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void playFile(String path)
	{
		resetBeforePlayDataSource();
		path = beforeSetDataSourceFile(path);
		try
		{
			mPlayer.setDataSource(path);
			start();
		} catch (Exception e)
		{
			notifyException(e);
		}
	}

	protected String beforeSetDataSourceFile(String path)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPlaying()
	{
		return mState == EnumState.Playing;
	}

	public boolean isPause()
	{
		return mState == EnumState.Paused;
	}

	/**
	 * 开始播放
	 */
	public void start()
	{
		switch (mState)
		{
		case Idle:

			break;
		case Initialized:
			prepareAsyncPlayer();
			break;
		case Preparing:

			break;
		case Prepared:

			break;
		case Playing:

			break;
		case Paused:
			startPlayer();
			break;
		case Completed:
			startPlayer();
			break;
		case Stopped:
			prepareAsyncPlayer();
			break;

		default:
			break;
		}
	}

	/**
	 * 暂停播放
	 */
	public void pause()
	{
		switch (mState)
		{
		case Idle:

			break;
		case Initialized:

			break;
		case Preparing:

			break;
		case Prepared:

			break;
		case Playing:
			pausePlayer();
			break;
		case Paused:

			break;
		case Completed:

			break;
		case Stopped:

			break;

		default:
			break;
		}
	}

	/**
	 * 停止播放
	 */
	public void stop()
	{
		switch (mState)
		{
		case Idle:

			break;
		case Initialized:

			break;
		case Preparing:

			break;
		case Prepared:
			stopPlayer();
			break;
		case Playing:
			stopPlayer();
			break;
		case Paused:
			stopPlayer();
			break;
		case Completed:
			stopPlayer();
			break;
		case Stopped:

			break;

		default:
			break;
		}
	}

	private void setState(EnumState state)
	{
		this.mState = state;
	}

	private void prepareAsyncPlayer()
	{
		setState(EnumState.Preparing);
		notifyPreparing();
		mPlayer.prepareAsync();
	}

	private void startPlayer()
	{
		setState(EnumState.Playing);
		notifyPlaying();
		mPlayer.start();
	}

	private void pausePlayer()
	{
		setState(EnumState.Paused);
		notifyPaused();
		mPlayer.pause();
	}

	private void stopPlayer()
	{
		setState(EnumState.Stopped);
		notifyStopped();
		mPlayer.stop();
	}

	private void resetPlayer()
	{
		setState(EnumState.Idle);
		notifyReset();
		mPlayer.reset();
	}

	// notify

	protected void notifyPreparing()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyPrepared()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyPlaying()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyPaused()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyCompletion()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyStopped()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyReset()
	{
		// TODO Auto-generated method stub
	}

	protected void notifyError(MediaPlayer mp, int what, int extra)
	{
		// TODO Auto-generated method stub
	}

	protected void notifyException(Exception e)
	{
		// TODO Auto-generated method stub
	}

	// listener
	private OnErrorListener mListenerOnError = new OnErrorListener()
	{

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra)
		{
			resetPlayer();
			notifyError(mp, what, extra);
			return true;
		}
	};

	private OnPreparedListener mListenerOnPrepared = new OnPreparedListener()
	{

		@Override
		public void onPrepared(MediaPlayer mp)
		{
			setState(EnumState.Prepared);
			notifyPrepared();
		}
	};

	private OnCompletionListener mListenerOnCompletion = new OnCompletionListener()
	{

		@Override
		public void onCompletion(MediaPlayer mp)
		{
			setState(EnumState.Completed);
			notifyCompletion();
		}
	};

	public enum EnumState
	{
		/** 空闲，还没设置dataSource */
		Idle,
		/** 已经设置dataSource，还未播放 */
		Initialized,
		/** 准备中 */
		Preparing,
		/** 准备完毕 */
		Prepared,
		/** 已经启动播放 */
		Playing,
		/** 已经暂停播放 */
		Paused,
		/** 已经播放完毕 */
		Completed,
		/** 调用stop方法后的状态 */
		Stopped;
	}

}

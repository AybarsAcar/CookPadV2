package com.aybarsacar.cookpadversion2.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


/**
 *
 */
@ActivityRetainedScoped
class Repository @Inject constructor(
  _remoteDataSource: RemoteDataSource,
  _localDataSource: LocalDataSource
) {

  // get a public variable to be accessed
  val remoteDataSource = _remoteDataSource
  val localDataSource = _localDataSource

}
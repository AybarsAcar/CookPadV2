package com.aybarsacar.cookpadversion2.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


/**
 *
 */
@ActivityRetainedScoped
class Repository @Inject constructor(_remoteDataSource: RemoteDataSource) {

  // get a public variable to be accessed
  val remoteDataSource = _remoteDataSource

}
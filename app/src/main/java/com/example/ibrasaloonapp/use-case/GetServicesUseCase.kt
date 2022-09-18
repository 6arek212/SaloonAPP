package com.example.ibrasaloonapp.use

import android.app.Application
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.KeyValueWrapper
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.services.WorkerService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private const val TAG = "GetServicesUseCase"


class GetServicesUseCase
@Inject
constructor(
    val workerService: WorkerService,
    val context: Application
) {

    suspend operator fun invoke(workerId: String) = flow {
        emit(Resource.Loading(true))

        val list = listOf(
            KeyValueWrapper("Hair Cut" , context.getString(R.string.hair_cut)),
            KeyValueWrapper("Wax" , context.getString(R.string.wax)),
        )

        emit(Resource.Success(data = list))


        emit(Resource.Loading(false))
    }


}
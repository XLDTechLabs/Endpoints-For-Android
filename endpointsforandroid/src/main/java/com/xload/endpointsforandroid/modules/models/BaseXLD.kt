package com.xload.endpointsforandroid.modules.models

import com.xload.endpointsforandroid.modules.interactor.BaseXLDInteractor
import com.xload.endpointsforandroid.modules.interactor.XLDInteractor
import com.xload.endpointsforandroid.modules.interactor.XLDInteractorImpl

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 05/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
abstract class BaseXLD: BaseXLDInteractor {

    override fun getInteractor(): XLDInteractor {
        return XLDInteractorImpl()
    }

}
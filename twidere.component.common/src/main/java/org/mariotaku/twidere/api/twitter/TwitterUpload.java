/*
 *                 Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2015 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.api.twitter;

import org.mariotaku.restfu.annotation.method.POST;
import org.mariotaku.restfu.annotation.param.Body;
import org.mariotaku.restfu.annotation.param.Part;
import org.mariotaku.restfu.http.BodyType;
import org.mariotaku.restfu.http.mime.FileTypedData;

import java.io.File;

import org.mariotaku.twidere.api.twitter.model.MediaUploadResponse;

@SuppressWarnings("RedundantThrows")
public interface TwitterUpload {

    @POST("/media/upload.json")
    @Body(BodyType.MULTIPART)
    MediaUploadResponse uploadMedia(@Part("media") File file) throws TwitterException;

    @POST("/media/upload.json")
    @Body(BodyType.MULTIPART)
    MediaUploadResponse uploadMedia(@Part("media") FileTypedData data) throws TwitterException;

}

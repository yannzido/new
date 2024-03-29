/**
 * The MIT License
 * Copyright (c) 2018 Estonian Information System Authority (RIA),
 * Nordic Institute for Interoperability Solutions (NIIS), Population Register Centre (VRK)
 * Copyright (c) 2015-2017 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.niis.xroad.restapi.converter;

import ee.ria.xroad.signer.protocol.message.GenerateCertRequest;

import lombok.Getter;
import org.niis.xroad.restapi.openapi.model.CsrFormat;

import java.util.Arrays;
import java.util.Optional;

import static ee.ria.xroad.signer.protocol.message.GenerateCertRequest.RequestFormat;

/**
 * Mapping between CsrFormat in api (enum) and model (RequestFormat)
 */
@Getter
public enum CsrFormatMapping {
    PEM(RequestFormat.PEM, CsrFormat.PEM),
    DER(RequestFormat.DER, CsrFormat.DER);

    private final CsrFormat csrFormat;
    private final GenerateCertRequest.RequestFormat requestFormat;

    CsrFormatMapping(RequestFormat requestFormat, CsrFormat csrFormat) {
        this.csrFormat = csrFormat;
        this.requestFormat = requestFormat;
    }

    /**
     * Return matching RequestFormat, if any
     * @param csrFormat
     * @return
     */
    public static Optional<RequestFormat> map(CsrFormat csrFormat) {
        return getFor(csrFormat).map(CsrFormatMapping::getRequestFormat);
    }

    /**
     * Return matching CsrFormat, if any
     * @param requestFormat
     * @return
     */
    public static Optional<CsrFormat> map(RequestFormat requestFormat) {
        return getFor(requestFormat).map(CsrFormatMapping::getCsrFormat);
    }

    /**
     * return CsrFormatMapping matching the given CsrFormat, if any
     * @param csrFormat
     * @return
     */
    public static Optional<CsrFormatMapping> getFor(CsrFormat csrFormat) {
        return Arrays.stream(values())
                .filter(mapping -> mapping.csrFormat.equals(csrFormat))
                .findFirst();
    }

    /**
     * return CsrFormatMapping matching the given RequestFormat, if any
     * @param requestFormat
     * @return
     */
    public static Optional<CsrFormatMapping> getFor(RequestFormat requestFormat) {
        return Arrays.stream(values())
                .filter(mapping -> mapping.requestFormat.equals(requestFormat))
                .findFirst();
    }

}

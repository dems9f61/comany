package com.takeaway.authorization.integrationsupport.boundary;

import com.takeaway.authorization.integrationsupport.entity.*;

/**
 * User: StMinko Date: 12.09.2019 Time: 10:41
 *
 * <p>
 */
public interface ServiceExceptionTranslator
{
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================

  default ApiException translateIntoApiException(ServiceException caught)
  {
    ServiceException.Reason reason = caught.getReason();
    switch (reason)
    {
      case SUB_RESOURCE_NOT_FOUND:
        return new BadRequestException(caught.getMessage());
      case RESOURCE_NOT_FOUND:
        return new ResourceNotFoundException(caught.getMessage());
      case INVALID_PARAMETER:
        return new BadRequestException(caught.getMessage(), caught.getCause());
      default:
        return new InternalServerErrorException(caught.getMessage());
    }
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}

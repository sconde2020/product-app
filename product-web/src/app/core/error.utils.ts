import { HttpErrorResponse } from '@angular/common/http';
import { ApiError } from '../product/model/api-error';
import { Observable, throwError } from 'rxjs';
import { ERROR_MESSAGES } from '../product/constants/error-message.constants';

/**
 * Transforms an HttpErrorResponse into an ApiError and returns it as an Observable error.
 *
 * @param error The HttpErrorResponse received from HttpClient.
 * @returns An Observable that throws an Error carrying the ApiError object.
 */
export function handleHttpError(error: HttpErrorResponse): Observable<never> {
  const apiError: ApiError = {};
  const errBody = error?.error;

  if (errBody) {
    if (typeof errBody === 'object') {
      apiError.timestamp = errBody.timestamp ?? undefined;
      apiError.status = errBody.status ?? error.status ?? undefined;
      apiError.message = errBody.message ?? error.message ?? undefined;
      apiError.path = errBody.path ?? undefined;
    } else if (typeof errBody === 'string') {
      try {
        const parsed = JSON.parse(errBody);
        apiError.timestamp = parsed.timestamp ?? undefined;
        apiError.status = parsed.status ?? error.status ?? undefined;
        apiError.message = parsed.message ?? parsed.error ?? errBody;
        apiError.path = parsed.path ?? undefined;
      } catch {
        apiError.message = errBody;
        apiError.status = error.status ?? undefined;
        apiError.timestamp = new Date().toISOString();
      }
    }
  } else {
    apiError.message = error.message ?? 'Unknown error';
    apiError.status = error.status ?? undefined;
    apiError.timestamp = new Date().toISOString();
  }

  const e = new Error(apiError.message ?? 'API error');
  (e as any).apiError = apiError;

  return throwError(() => e);
}

/**
 * Extracts a user-friendly error message from an ApiError and returns it as an Observable error.
 *
 * @param error The ApiError object.
 * @returns An Observable that throws an Error with a user-friendly message.
 */
export function handleApiError(error: ApiError): Observable<never> {
  const message = error?.message || ERROR_MESSAGES.UNKNOWN_ERROR;
  return throwError(() => new Error(message));
}

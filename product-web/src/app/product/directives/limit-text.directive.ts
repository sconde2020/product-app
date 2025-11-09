import { Directive, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appLimitText]'
})
export class LimitTextDirective {

  @Input('appLimitText') maxLength = 0;

  constructor() {}

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    const input = event.target as HTMLInputElement | HTMLTextAreaElement;
    if (this.maxLength && input.value.length > this.maxLength) {
      input.value = input.value.substring(0, this.maxLength);

      // trigger re-render for ngModel or reactive forms
      const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value')?.set;
      nativeInputValueSetter?.call(input, input.value);
      input.dispatchEvent(new Event('input', { bubbles: true }));
    }
  }

  
}

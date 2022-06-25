import { Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: 'img[appHideMissing]',
})
export class HideMissingDirective {
  constructor(private el: ElementRef) {}

  @HostListener('error')
  private onError() {
    this.el.nativeElement.parentNode.style.display = 'none';
  }
}

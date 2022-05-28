import {ChangeDetectorRef, Component, OnInit, TemplateRef} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {MessageDto} from '../../dtos/messageDto';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  error = false;
  errorMessage = '';
  // After first submission attempt, form validation will start
  submitted = false;

  currentMessage: MessageDto;

  private message: MessageDto[];

  constructor(private messageService: MessageService,
              private ngbPaginationConfig: NgbPaginationConfig,
              private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef,
              private authService: AuthService,
              private modalService: NgbModal) {
  }

  ngOnInit() {
    this.loadMessage();
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  openAddModal(messageAddModal: TemplateRef<any>) {
    this.currentMessage = new MessageDto();
    this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openExistingMessageModal(id: number, messageAddModal: TemplateRef<any>) {
    this.messageService.getMessageById(id).subscribe({
      next: res => {
        this.currentMessage = res;
        this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  /**
   * Starts form validation and builds a message dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addMessage(form) {
    this.submitted = true;


    if (form.valid) {
      this.currentMessage.publishedAt = new Date().toISOString();
      this.createMessage(this.currentMessage);
      this.clearForm();
    }
  }

  getMessage(): MessageDto[] {
    return this.message;
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  /**
   * Sends message creation request
   *
   * @param message the message which should be created
   */
  private createMessage(message: MessageDto) {
    this.messageService.createMessage(message).subscribe({
        next: () => {
          this.loadMessage();
        },
        error: error => {
          this.defaultServiceErrorHandling(error);
        }
      }
    );
  }

  /**
   * Loads the specified page of message from the backend
   */
  private loadMessage() {
    this.messageService.getMessage().subscribe({
      next: (message: MessageDto[]) => {
        this.message = message;
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }


  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

  private clearForm() {
    this.currentMessage = new MessageDto();
    this.submitted = false;
  }

}

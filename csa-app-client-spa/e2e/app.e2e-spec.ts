import { CsaAppClientSpaPage } from './app.po';

describe('csa-app-client-spa App', function() {
  let page: CsaAppClientSpaPage;

  beforeEach(() => {
    page = new CsaAppClientSpaPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});

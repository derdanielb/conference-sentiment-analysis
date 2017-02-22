import { CsaSpaClientPage } from './app.po';

describe('csa-spa-client App', function() {
  let page: CsaSpaClientPage;

  beforeEach(() => {
    page = new CsaSpaClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});

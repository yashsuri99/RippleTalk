import { RegisterFormFour } from '../features/register/components/RegisterForms/RegisterFormFour';
import { RegisterFormOne } from '../features/register/components/RegisterForms/RegisterFormOne';
import { RegisterFormThree } from '../features/register/components/RegisterForms/RegisterFormThree';
import { RegisterFormTwo } from '../features/register/components/RegisterForms/RegisterFormTwo';
import data from '../data/codes.json';
import { RegisterFormFive } from '../features/register/components/RegisterForms/RegisterFormFive';
import { RegisterFormSix } from '../features/register/components/RegisterForms/RegisterFormSix';

export const determineModalContent = (step: number): JSX.Element => {
	switch (step) {
		case 1:
			return <RegisterFormOne />;

		case 2:
			return <RegisterFormTwo />;

		case 3:
			return <RegisterFormThree />;

		case 4:
			return <RegisterFormFour />;

		case 5:
			return <RegisterFormFive />;

		case 6:
			return <RegisterFormSix />;

		default:
			return <></>;
	}
};

export const countryCodeDropDown = (): JSX.Element[] => {
	let options = data
		.filter((country) => {
			if (country.code !== 'India') {
				return country;
			}
		})
		.map((country) => {
			return (
				<option
					value={`${country.dial_code} ${country.name}`}
					key={country.code}
				>
					{`${country.dial_code} ${country.name}`}
				</option>
			);
		});

	options.unshift(
		<option value={'+91 India'} key={'India'}>
			{'+91 India'}
		</option>
	);

	return options;
};
